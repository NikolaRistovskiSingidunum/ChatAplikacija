// Create WebSocket connection.

const sockethost = "ws://localhost:8887";
const springservice = "http://localhost:9090/";
//socket = new WebSocket('ws://localhost:8887');
//
//// Connection opened
//socket.addEventListener('open', function (event) {
//    socket.send(document.cookie);
//});
//
//// Listen for messages
//socket.addEventListener('message', function (event) {
//    console.log('Message from server ', event.data);
//});
//
//socket.addEventListener('close', function (event) {
//    console.log('socket closed  ', event.data);
//    setTimeout(() => { console.log("Creating new socket!"); socket = new WebSocket('ws://localhost:8887');
//  }, 2000);
//});


//socket.onclose = function(event)
//{
//	console.log("soket je zatvoren");
//}
var ws;
function startWebsocket() {
	  ws = new WebSocket(sockethost);

	  ws.onmessage = function(e){
	    console.log('websocket message event:', e);
	  }

	  ws.onclose = function(e){
	    // connection closed, discard old websocket and create a new one in 5s
		console.log('socket je zatvoren', e);
	    ws = null;
	    setTimeout(startWebsocket, 2000);
	  }
	  
	  ws.onopen = function(){
		  var cookie = getCookie('JSESSIONID');
		  ws.send(cookie);
	  }
	}

startWebsocket();


function getCookie(NameOfCookie)
{
    if (document.cookie.length > 0)
{
    begin = document.cookie.indexOf(NameOfCookie+"=");
    if (begin != -1)
   {
    begin += NameOfCookie.length+1;
      end = document.cookie.indexOf(";", begin);
      if (end == -1) end = document.cookie.length;
      return unescape(document.cookie.substring(begin, end));      
      }
  }
return null; 
}



function sendLoginCookie(msg)
{
	
	var cookie = {"jsessionid":getCookie('JSESSIONID')};
	cookie = JSON.stringify(cookie);
	console.log(cookie);
	ws.send(cookie);
}

function send()
{
	var msg = { "id":[1,2],"message":"neka poruka"};
	msg = JSON.stringify(msg);
	console.log(msg);
	ws.send(msg);
	
}

function login()
{
	let xhttp = new XMLHttpRequest();
	xhttp.open("POST", "http://localhost:9090/"+"login", true);
	xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xhttp.send("username=nikola&password=12345");
}
function logout()
{
}