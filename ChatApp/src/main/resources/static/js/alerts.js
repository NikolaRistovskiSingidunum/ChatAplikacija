function showDiv() {
  let div = document.createElement("div");
  div.innerHTML = '<div id="newAlert" class="alert alert-info">Hello</div>'
  document.body.appendChild(div);
  $("#newAlert").delay(3000).fadeOut('slow', function() {
    $(this).remove();
  });
}