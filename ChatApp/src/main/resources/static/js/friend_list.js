

var view = '<button type="button" class="btn btn-primary btn-block color-pink">Primary</button>';

function aaa()
{
	console.log("daaaa");
}

function getFriendList()
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() { 
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
        	onGetFirendList(xmlHttp.responseText);
    }
    xmlHttp.open("GET", springservice+"allusers", true); // true for asynchronous 
    xmlHttp.send(null);
}


//function onGetFirendList(data)
//{
//	data =  JSON.parse(data);
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
//    
//	
//}

function copy()
{
	
	let el = $( "#view1" ).clone();
	el.css("visibility","visible");
	$("body").append(el);
}


