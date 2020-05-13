

var template_userdetails = '<div class="user-details" >\
							<h2>Natasa</h2>\
							<a href="#" onclick="logout()" class="btn btn-primary">Odjava</a>\
							</div>';

function whoAmI() {
	console.log("opet JA i samo JA");

	
	let xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			// Typical action to be performed when the document is ready:
			me = JSON.parse(xhttp.responseText);
			populateUserInfo(me);
			console.log( xhttp.responseText );
			//alert(me.id);
		}
	};

	xhttp.open("GET", springservice+"whoAmI",false);
	xhttp.send();

}
//dobij i stavi ih na ekran
function populateUserInfo(me)
{
	let ja = DomParser.parseFromString(template_userdetails, "text/html").body.childNodes[0];
	
    $(ja).find("h2").text(me.fullname.split(" ")[0]);
    
    $("body").append(ja);
    
}