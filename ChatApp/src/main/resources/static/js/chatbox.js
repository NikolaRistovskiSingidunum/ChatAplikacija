

//var template_chat_box = '<textarea  class="chatbox" rows="4" cols="50"> \
//Koji kurac.\
//</textarea>';
//boksovi su tipa DomElement
class ActiveChatboxes
    {
		constructor()
        {
            this.active_chatboxes = [];
            this.width = 200;
        }
        activate(box)
        {
        	let bInGoodPosition = false
            for(let i=0; i<this.active_chatboxes.length &&  i<6; i++)
            	{
            	if(this.active_chatboxes[i] == box)
            		bInGoodPosition = true;
            	}
            if(bInGoodPosition == false)
                {
                    this.moveBoxToFristPlace(box);
                }
            this.redraw();
            this.deleteOverflow();
            maximizeChatBox(box);
        }
        moveBoxToFristPlace(box)
        {
            let index = this.active_chatboxes.indexOf(box);
            if(index != -1)
            	{
            this.active_chatboxes.splice(index, 1);
            	}
            this.active_chatboxes.unshift(box);
        }
        redraw()
        {
                for(let i=0; i<this.active_chatboxes.length &&  i<6; i++)
            	{
                	let box = this.active_chatboxes[i];
                    $(box).css("position","fixed");
                    $(box).css("right", 200+ i*this.width );
                    $(box).css("bottom",0);
                    $(box).css("visibility","visible");
                    
                    
                    let target = $(box).find(".msg-box");
                    if(target.css("display")=="none")
                    	$(box).find(".msg-icon-minimize").css("visibility","hidden");
                    else
                    	$(box).find(".msg-icon-minimize").css("visibility","visible");
                    
                    
                	$(box).find(".msg-icon-delete").css("visibility","visible");
                    //$("body").append($(newChatBox));
            	}
        }
        
        deleteOverflow()
        {
           let l = this.active_chatboxes.length;
            for(let i=6; i<l;i++ )
                {
                    $(this.active_chatboxes[i]).css("visibility","hidden");
                	$(this.active_chatboxes[i]).find(".msg-icon-minimize").css("visibility","hidden");
                	$(this.active_chatboxes[i]).find(".msg-icon-delete").css("visibility","hidden");
                }
                
                
            if(l<6)
                return;
            else
                this.active_chatboxes.splice(6,l);
                
        }
        remove(box)
        {
            let index = this.active_chatboxes.indexOf(box);
            if(index != -1)
            	{
            	$(box).css("visibility","hidden");
            	this.active_chatboxes.splice(index, 1);
            	//vidljivost se ne propagira na dete element
            	$(box).find(".msg-icon-minimize").css("visibility","hidden");
            	$(box).find(".msg-icon-delete").css("visibility","hidden");
            	}
            this.redraw();
        }
    }

var activeChatBoxes = new ActiveChatboxes();
var template_chat_box = '<div class="msg-whole-chat-box" draggable="false">\
	<i class="fas fa-times msg-icon-delete" onclick="activeChatBoxes.remove(this.parentNode)" title="skloni" ></i>\
	<i class="far fa-minus-square msg-icon-minimize" onclick="minimizeChatBox(this.parentNode)" title="smanji"></i>\
			<div class="msg-box">\
				<label class="msg-label"> ime prijatelja</label>\
				<div class="msg-div-more">\
				<i class="fas fa-angle-double-up msg-icon-more" title="prikazi jos porukua"></i><br\>\
				</div>\
				<div class="msg-read"></div>\
				\
				<div>\
	<div>\
	<i class="fas fa-paperclip msg-icon-attach" title="prvuci fajl na kutiju ispod da ga zakacis" ><input class="manual-attachment" type="file" accept="image/*,.pdf" multiple></i>\
	</div>\
    <div class="msg-attachment" ondrop="drop(event,this)" ondragover="allowDrop(event)"></div>\
	<div contenteditable="true" class="msg-input"  ondragover="allowDrop(event)" ></div>\
	</div>\
			</div>\
			<div>\
				<button class="msg-dugme" onclick="toggleVisibility(this, this.parentNode.parentNode)">prijatelj</button>\
			</div>\
		</div>';
var template_bottom_chat_box = '<button> Neki tekst </button>';
var template_friend_selector = '<button class="friend-selector"> Ime Prijatelja </button>';
//map svih prijatelja i 
var chatboxes = new Map();
//var active_chatboxes = new Map();
//treba da ih bude 10 po strani u default implementaciji - horizontalno jedna ispod druge
function test_draw10()
{
    for(let i=0; i< 6 ; i++)
    {
    let newChatBox = DomParser.parseFromString(template_chat_box, "text/html").body.childNodes[0];
        $(newChatBox).css("position","fixed");
        $(newChatBox).css("right", 200+ i*150 );
        $(newChatBox).css("bottom",0);
        $("body").append($(newChatBox));
    }
}



function onGetFirendList(data)
{
	data =  JSON.parse(data);
	for (let i = 0; i < data.length; i++) 
		{
		//createChatBox_new(data[i]);
        createFriendSelector(data[i]);
		}
	
	loadLast50forAll();
//	let lista = document.getElementById("friend-list");
//	let lista1 = $("#friend-list");
//	lista.innerHTML="";
//    console.log("duzina lista" + data.length);
//    for (let i = 0; i < data.length; i++) {
//    	let el = DomParser.parseFromString(view,"text/html").body.childNodes[0];
//    	let info = data[i];
//    	console.log(info);
//    	let el1 = $("#view2").clone();
//    	el1.css("visibility","visible");
//        el.innerHTML = data[i].username;
//        el["info"] = info;
//        el.addEventListener("click",function(event) { alert(info.username); console.log(this.info);    });
////        lista.appendChild(el1);
//        lista1.append(el);
//    console.log(data[i].username);
//    //Do something
//    }
    
	
}
function createFriendSelector(user)
{
    	let b = chatboxes.has(user.username);
	if(b==true)
		{
		console.log(user.username + " vec postoji u mapi " );
		return false;
		}
	//ako ne postoji takav user kreiraj za njega ui
	//chatboxes.set(user.username, newChatBox );
	
    let newFriendSelector = DomParser.parseFromString(template_friend_selector, "text/html").body.childNodes[0];
    newFriendSelector["user"] = user;
    $(newFriendSelector).text(user.fullname);
    
    
  //ako ne postoji takav user kreiraj za njega ui
    let newChatBox = createChatBox_new_new(user);
    chatboxes.set(user.username, newChatBox );
    
    newFriendSelector["chatbox"]=newChatBox;
    
    $(newFriendSelector).click(function() { console.log(newChatBox.user); activeChatBoxes.activate(newChatBox);  })
    
    
    
    
    let ch = $(".friend-list");
    ch.append($(newFriendSelector));
    
    
    
    
}
function minimizeChatBox(box)
{
	let target = $(box).find(".msg-box");
    target.css("display","none");
    let msgbutton = $(box).find(".msg-dugme"); 
    //msgbutton.text(msgbutton.attr("username"));
    $(box).find(".msg-icon-minimize").css("visibility","hidden");
}
function maximizeChatBox(box)
{
	let target = $(box).find(".msg-box");
    target.css("display","block");
    let msgbutton = $(box).find(".msg-dugme");
    //msgbutton.text("sakrij");
    $(box).find(".msg-icon-minimize").css("visibility","visible");
}
function toggleVisibility(el, p)
{
//	console.log(p);
//	console.log("AAAAAAA");
//	//alert("koji je opet kurac");
//	let parent = el.parentElement.parentElement;
//	parent  = parent.parentElement;
	//console.log(parent);
	//console.log(el.childNodes);
    //let target = p.childNodes[1];
    let target = $(p).find(".msg-box");
    //console.log(target);
    //target = $(target);
    //alert(target.css("display"));
    
    //postavi da bude vidljivost
    if(target.css("display")=="none")
    	{
        target.css("display","block");
        //$(el).text("sakrij");
        $(p).find(".msg-icon-minimize").css("visibility","visible");
    	}
    else
    	{
    	
        target.css("display","none");
        //$(el).text($(el).attr("username"));
        $(p).find(".msg-icon-minimize").css("visibility","hidden");
    	}
}

//vraca true ako je korisnik uspesno kreiran
//function createChatBox_new(user)
//{
//	let b = chatboxes.has(user.username);
//	if(b==true)
//		{
//		console.log(user.username + " vec postoji u mapi " );
//		return false;
//		}
//	
//    let newChatBox = DomParser.parseFromString(template_chat_box, "text/html").body.childNodes[0];
//    newChatBox["user"] = user;
//    console.log(newChatBox["user"]);
//    //dodaj
//    chatboxes.set(user.username, newChatBox );
//    $(newChatBox).find("label").text(user.fullname);
//    $(newChatBox).find("button").attr("username",user.username);
//    $(newChatBox).find("button").text(user.fullname);
//    console.log($(newChatBox).find("label"));
//    let ch = $(".friend-list");
//    ch.append($(newChatBox));
//    
//    return true;
//}
function createChatBox_new_new(user)
{
    let newChatBox = DomParser.parseFromString(template_chat_box, "text/html").body.childNodes[0];
    newChatBox["user"] = user;
    console.log(newChatBox["user"]);
    //dodaj
    chatboxes.set(user.username, newChatBox );
    $(newChatBox).find("label").text(user.fullname);
    $(newChatBox).find("button").attr("username",user.username);
    $(newChatBox).find("button").text(user.fullname);
    console.log($(newChatBox).find("label"));
    $(newChatBox).css("visibility","hidden");
    
    
//    msg-icon-attach" title="prvuci fajl na kutiju ispod da ga zakacis" ><input class="manual-attachment"

    //postavi file picker
    
    let icon_attach = $(newChatBox).find(".msg-icon-attach");
    let input_attach = $(newChatBox).find(".manual-attachment");  
    icon_attach.get(0).onclick = function(){ input_attach.get(0).click(); };
    
    input_attach.get(0).addEventListener("change", function(){
        for(let i=0; i< input_attach.get(0).files.length ;i++)
            addFileToMessageList(input_attach.get(0).files[i], $(newChatBox).find(".msg-attachment").get(0));
    }
                                         , false);
    
//    addFileToMessageList(file, msgList)
    
    //ovo je kod za zakacinjenje - ako budemo brisali moracemo i ovo da obrisemo
    let attachement =$(newChatBox).find(".msg-attachment").get(0);
    $(newChatBox).find(".msg-input").on('drop', function() { drop(event,attachement); } );
    //$("body").append($(newChatBox));
//    console.log($(newChatBox).find(".msg-input"));
//    $(newChatBox).find(".msg-input").css("color","blue");
    
    $(newChatBox).find(".msg-input").keypress(function(event){
        let keycode = (event.keyCode ? event.keyCode : event.which);
        //console.log(keycode);
        if(keycode == '13'){
            //alert($(newChatBox).find(".msg-input").val());
            sendAllAttachment(attachement,user.id);
            let msg = {};
            msg["text"] = $(newChatBox).find(".msg-input").text().trim();
            msg["receiverid"] =  user.id;
            
            //console.log(msg["text"].trim());
            if(msg["text"].length > 0)
            {
            sendMessage(msg);
            $(newChatBox).find(".msg-input").text("");
            }
        }

    });
    
    $(newChatBox).find(".msg-input").keydown(function(event){
    	let keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == '40')
    	{
    	//alert("aaa");
    	let text = $(newChatBox).find(".msg-input").text();
    	$(newChatBox).find(".msg-input").text(text+"\n");
    	
    	}
    });
    
    
    $("body").append($(newChatBox));
    
    onChatBoxCreated(newChatBox);
    return newChatBox;
}
function sendAllAttachment(attachmentList, receiverid)
{
	attachmentList = $(attachmentList);
	let children = attachmentList.children();
    //console.log(children);
    for( let i=0; i< children.length; i++)
    	{
        console.log(children[i]);
        sendFile(children[i].file,receiverid);
        $(children[i]).remove();
    	}
}
function sendFile(file,receiverid)
{
	 let formdata = new FormData();
	
	 let f = {};
	 f["file"] = file;
	 f["receiverid"] = receiverid;
	 formdata.append("receiverid",receiverid);
	 formdata.append("file", file);
	 formdata.append("text", file.type)
	  $.ajax({
		    type: "POST",
		    url: springservice+"file-proxy",
		    data: formdata,
		    contentType: false,
		    processData: false,
		    async: false
		
		});
	  
	  
	  console.log(formdata);
}
//function sendMessage(msg)
//{
//	
//	  $.ajax({
//		    type: "POST",
//		    url: springservice+"message-proxy",
//		    data: msg
//		
//		});
//}
//function getMessage(msgID)
//{
//	  $.ajax({
//		    type: "GET",
//		    url: springservice+"message/"+msgID,
//		    success: function (response) { console.log(response); }
//		
//		});
//}
var chatBoxToggler = [];



//function createChatBox(el)
//{
//	
//	
//	let newChatBox = DomParser.parseFromString(template_chat_box, "text/html").body.childNodes[0];
//	let jqEl = $(el);
//	let domRect = el.getBoundingClientRect();
//    console.log(domRect);
////	if(newChatBox instanceof Element || newChatBox instanceof HTMLDocument)
////		alert("koji kurac");
//	let w = newChatBox.style.width;
//    let h = newChatBox.style.height;
//    let left = domRect.left;
//    //left = jqEl.offset().left;
//    //left = 200;
//    alert(left);
//    let top  = domRect.top;
//    let h1 = el.style.height;
//    //newChatBox.style.position = "fixed";
//    newChatBox.style.left= left;
//    newChatBox.style.top = top - 150; 
//	document.body.appendChild(newChatBox);
//}
var createdChatBoxes = [];
function onChatBoxCreated(box)
{
	createdChatBoxes.push(box);
}
function getChatBoxByUserID(userID)
{
	for(let i=0; i<createdChatBoxes.length; i++)
		if(createdChatBoxes[i].user.id == userID)
			return createdChatBoxes[i];
}
function getFullNameFromID(userID)
{
	for(let i=0; i<createdChatBoxes.length; i++)
		if(createdChatBoxes[i].user.id == userID)
			return createdChatBoxes[i].user.fullname;
}
function getFirstNameFromID(userID)
{
	let fullname = getFullNameFromID(userID);
	return fullname.split(" ")[0];
}
