
function aa()
{
	alert("AAAAAAAA");
}

class ObjectTemplate
{
	//prosledujemo template id
	
	
	
	constructor(templateID)
	{
		let lista1 = $("#friend-list");
		let el = $( "#"+templateID ).clone();
		//console.log(el.prop());
		for(var key in el[0]) {
		    var value = el[0][key];
		    //console.log(typeof value);
		    if(typeof value === 'string' )
		    	{
		    	//alert("AAAAAAAAAAAAA");
		    	console.log(value);
            var res = value.match(/this.aa()/);
            if(res!=null)
		    console.log("bbbbbbb" + value);
		    	}
		}
		//return;
		el.css("visibility","visible");
		console.log(el);
		let allElemetns = el.get();
		for(let i=0; i < allElemetns.length; i++)
			{
			let domElement = allElemetns[i];
			
			console.log("gggg" + domElement);
			console.log("gggg" + domElement.tagName);
			alert(domElement.eventListenerList);
			domElement.addEventListener("click", function(){ alert("Hello World!"); }); 
			//domElement.prototype.bind(this);
			
			}
		
		lista1.append(el);
		//template = el;
	}
	
	aaa()
	{
		alert("AAAAa");
	}
//	
//	render(parentID)
//	{
//		$("#"+parentID).append(template);
//	}
//	
//	get template()
//	{
//		return this.template;
//	}
//	
//	set template()
//	{
//		return this.template;
//	}
	
	
}


function test()
{
	let obj = new ObjectTemplate("view2");
}