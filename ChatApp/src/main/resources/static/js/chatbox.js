

//var template_chat_box = '<textarea  class="chatbox" rows="4" cols="50"> \
//Koji kurac.\
//</textarea>';
//boksovi su tipa DomElement
class ActiveChatboxes
    {
		constructor()
        {
            this.active_chatboxes = [];
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
                    $(box).css("right", 200+ i*150 );
                    $(box).css("bottom",0);
                    $(box).css("visibility","visible");
                    //$("body").append($(newChatBox));
            	}
        }
        
        deleteOverflow()
        {
           let l = this.active_chatboxes.length;
            for(let i=6; i<l;i++ )
                {
                    $(this.active_chatboxes[i]).css("visibility","hidden");
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
            	$(this.active_chatboxes[index]).css("visibility","hidden");
            	this.active_chatboxes.splice(index, 1);
            	}
            this.redraw();
        }
    }

var activeChatBoxes = new ActiveChatboxes();
var template_chat_box = '<div class="msg-whole-chat-box" draggable="true">\
	<i class="fas fa-times msg-icon-delete" onclick="activeChatBoxes.remove(this.parentNode)" ></i>\
	<i class="far fa-minus-square msg-icon-minimize" onclick="minimizeChatBox(this.parentNode)"></i>\
			<div class="msg-box">\
				<label class="msg-label"> ime prijatelja</label>\
				<textarea readonly class="msg-read" ></textarea>\
				<br />\
				<textarea class="msg-input" ></textarea>\
			</div>\
			<div>\
				<button class="msg-dugme" onclick="toggleVisibility(this, this.parentNode.parentNode)">prijatelj</button>\
			</div>\
		</div>';
var template_bottom_chat_box = '<button> Neki tekst </button>';
var template_friend_selector = '<button> Ime Prijatelja </button>';
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
    msgbutton.text(msgbutton.attr("username"));
    $(box).find(".msg-icon-minimize").css("visibility","hidden");
}
function maximizeChatBox(box)
{
	let target = $(box).find(".msg-box");
    target.css("display","block");
    let msgbutton = $(box).find(".msg-dugme");
    msgbutton.text("sakrij");
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
        $(el).text("sakrij");
        $(p).find(".msg-icon-minimize").css("visibility","visible");
    	}
    else
    	{
    	
        target.css("display","none");
        $(el).text($(el).attr("username"));
        $(p).find(".msg-icon-minimize").css("visibility","hidden");
    	}
}

//vraca true ako je korisnik uspesno kreiran
function createChatBox_new(user)
{
	let b = chatboxes.has(user.username);
	if(b==true)
		{
		console.log(user.username + " vec postoji u mapi " );
		return false;
		}
	
    let newChatBox = DomParser.parseFromString(template_chat_box, "text/html").body.childNodes[0];
    newChatBox["user"] = user;
    console.log(newChatBox["user"]);
    //dodaj
    chatboxes.set(user.username, newChatBox );
    $(newChatBox).find("label").text(user.fullname);
    $(newChatBox).find("button").attr("username",user.username);
    $(newChatBox).find("button").text(user.fullname);
    console.log($(newChatBox).find("label"));
    let ch = $(".friend-list");
    ch.append($(newChatBox));
    
    return true;
}
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
    $("body").append($(newChatBox));
    return newChatBox;
}
var chatBoxToggler = [];



function createChatBox(el)
{
	
	
	let newChatBox = DomParser.parseFromString(template_chat_box, "text/html").body.childNodes[0];
	let jqEl = $(el);
	let domRect = el.getBoundingClientRect();
    console.log(domRect);
//	if(newChatBox instanceof Element || newChatBox instanceof HTMLDocument)
//		alert("koji kurac");
	let w = newChatBox.style.width;
    let h = newChatBox.style.height;
    let left = domRect.left;
    //left = jqEl.offset().left;
    //left = 200;
    alert(left);
    let top  = domRect.top;
    let h1 = el.style.height;
    //newChatBox.style.position = "fixed";
    newChatBox.style.left= left;
    newChatBox.style.top = top - 150; 
	document.body.appendChild(newChatBox);
}