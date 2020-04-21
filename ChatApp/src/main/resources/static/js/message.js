function testMe()
{
	console.log("zdravo gde si mi ti ");
}



var template_message = '<div class="msg-container">\
						<p class="msg-name"></p>\
						<p class="msg-msg"> Ovo je neki tekst  </p>\
						<p class="msg-status"> status poruke </p>\
						</div>';

//kreira poruku po sablonu
function create_message(text)
{
	let newMessage = DomParser.parseFromString(template_message, "text/html").body.childNodes[0];
	$(newMessage).find(".msg-msg").text(text);
	
	
	return newMessage; 
	//$("body").append($(newMessage));
}
function messageSetTextAndName(msg,text,name)
{
	$(msg).find(".msg-msg").text(text);	
	$(msg).find(".msg-name").text(name);	
}
//let newChatBox = DomParser.parseFromString(template_chat_box, "text/html").body.childNodes[0];

function sendMessage(msg)
{
	
	  $.ajax({
		    type: "POST",
		    url: springservice+"message-proxy",
		    data: msg
		
		});
}
function getMessage(msgID)
{
	  $.ajax({
		    type: "GET",
		    url: springservice+"message/"+msgID,
		    success: onGetMessage
		
		});
}
//ova fukcija se poziva kada je lista prijatelja dostavljenja
function loadLast50forAll()
{
	for(let i=0; i<createdChatBoxes.length; i++)
		{
		let friendid = createdChatBoxes[i].user.id;
		getMessagesLast50(friendid);
		}
}
function getMessagesLast50(friendID)
{
	  $.ajax({
		    type: "GET",
		    url: springservice+"message-last/"+friendID,
		    success: onGetMessagesLast50
		
		});
}
function onGetMessagesLast50(response)
{
	console.log(response);
	for(let i=0; i< response.length ; i++)
		onGetMessage(response[i],true);
}


//reposne je vec js objekat
function onGetMessage(response,preLoading)
{
	let msgType = response.messagetype;
	let box;
	let msg = create_message(response.text + "  " + response.messageid);
	let firstname;
	if(msgType == "TO_ME")
		{
		box = getChatBoxByUserID(response.senderid);
		$(msg).css("color","green");
		$(msg).css("float","right");
		$(msg).find(".msg-name").css("float","right");
		$(msg).find(".msg-msg").css("clear","both");
		firstname = getFirstNameFromID(response.senderid);
		
		}
	else
		{
		box =getChatBoxByUserID(response.receiverid);
		$(msg).css("color","brown");
		firstname = getFirstNameFromID(response.senderid);
		}
	messageSetTextAndName(msg, response.text + "  " + response.messageid,firstname+":" );
	//let box = getChatBoxByUserID(response.receiverid);
	let read = $(box).find(".msg-read");
	//let msg = create_message(response.text + "  " + response.messageid);
	msg.messageid = response.messageid;
	
	
	//prikazi prozor kada stigne poruka ako nismo u loadovanju
	if(preLoading !== true)
		{
		effectsWhenMessageArrives(box);
		}
	
	placeMsgToRightPlace(box, msg);
	//read.append(msg);
	//response = JSON.parse(response);
	//console.log("neka poruka");
	//console.log(response.receiverid);
	//console.log("neka poruka");
}
function effectsWhenMessageArrives(box)
{
	activeChatBoxes.activate(box);
}
//stavi poruku na svoje mesto u kutiji, izmedju dve poruke - ako je poruka sa id =255 ona ide izmedju dve uzastopne npr. 234 - 264
//poruke nisu uzastopne jer su u tabeli
//box je dom(js) elementar, msg je dom elemnat
function placeMsgToRightPlace(box, msg)
{
	msg = $(msg);
	let msgid = msg.get(0).messageid;
	let read = $(box).find(".msg-read");
	let deca = read.find("div"); //children 
	//let deca1 = jQuery.makeArray(deca); // deca.makeArray();
	//deca1.sort(function(a, b){return a.messageid-b.messageid});
	let l =deca.length;
	if(l==1)
		{
		if(deca[0].messageid>msgid) // 85< 64
			{
			msg.insertBefore(deca[0]);
			}
		else
			{
			read.append(msg);
			}
		return;
		}
	if(l>=1 && deca[0].messageid > msgid)
		{
		msg.insertBefore(deca[0]);
		return;
		}
	for (let i=0; i< deca.length-1; i++)
		{
		let g = deca[i+1].messageid;
			if( (deca[i].messageid > msgid) && (deca[i+1].messageid < msgid) )
				{
				 msg.insertAfter(deca[i]);
				 return;
				}
			if( (deca[i].messageid == msgid) || (deca[i+1].messageid == msgid) )
				return;
		}
	
	read.append(msg);
	//read.get(0).scrollTop = 1000;
	msg.get(0).scrollIntoView();
	
}
function messageRouter(msg)
{
	msg = JSON.parse(msg.data);
//	console.log("poruka " + msg.data);
//	console.log(msg.messagetype);
	if(msg.messagetype == "GET_MESSAGE")
		{
		 getMessage(msg.messageid);
		 return;
		}
}