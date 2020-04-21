function dragStart(event) {
  event.dataTransfer.setData("Text", event.target.id);
  document.getElementById("demo").innerHTML = "Started to drag the p element";
}

function allowDrop(event) {
  event.preventDefault();
}

const validImageTypes = ['image/gif', 'image/jpeg', 'image/png'];
const validFileTypes = []; //to do
//ovo se odnosi na multipar
var template_image = '<div>\
                    <img class="msg-img" ></img>\
					<i class="fas fa-times msg-file-delete" onclick="$(this.parentNode).remove();" title="skloni" ></i>\
                         </div>';
                        
function drop(event,target) {
	console.log(event.dataTransfer.files[0]);
	console.log(event.dataTransfer.files[0]['type']);
	
	for( let i=0; i< event.dataTransfer.files.length; i++ )
	addFileToMessageList(event.dataTransfer.files[i],target);
	return;
    let template = DomParser.parseFromString(template_image, "text/html").body.childNodes[0];
    let img = $(template).find(".msg-img").get(0);
	event.preventDefault();
	let localURL = URL.createObjectURL(event.dataTransfer.files[0]);
	if (validImageTypes.includes(event.dataTransfer.files[0]['type'])) {
	    // invalid file type code goes here.
		   img.src = URL.createObjectURL(event.dataTransfer.files[0]);
		   
		   //img.title = "AAAAA";
		   //$( target ).append(template);
	}
	else
	{
		   img.src = "/img/file.png";
		   //$( target ).append(template);
	}
	img.onclick = function () { open(localURL); };
	img.title = event.dataTransfer.files[0]['name'];
	$( target ).append(template);
}
function addFileToMessageList(file, msgList)
{
	event.preventDefault();
	//let target = msgList;
    let template = DomParser.parseFromString(template_image, "text/html").body.childNodes[0];
    let img = $(template).find(".msg-img").get(0);
	
	let localURL = URL.createObjectURL(file);
	if (validImageTypes.includes(file['type'])) {
	    // invalid file type code goes here.
		   img.src = URL.createObjectURL(file);
		   
		   //img.title = "AAAAA";
		   //$( target ).append(template);
	}
	else
	{
		   img.src = "/img/file.png";
		   //$( target ).append(template);
	}
	template["file"] = file;
	img.onclick = function () { open(localURL); };
	img.title = file['name'];
	$( msgList ).append(template);
}