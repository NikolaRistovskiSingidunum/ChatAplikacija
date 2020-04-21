//ovaj file se poziva kada se stranica u ucita i sluzi za inicijalizaciju 
//this file is first to call when the application starts


window.onload = (event) => {
	
	whoAmI();
	getFriendList();
	
    
     startWebsocket();
};