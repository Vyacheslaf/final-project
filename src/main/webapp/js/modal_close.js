var modal = document.getElementById('id01');
var modalReg = document.getElementById('id02');

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
  if (event.target == modal) {
    modal.style.display = "none";
  }
  if (event.target == modalReg) {
    modalReg.style.display = "none";
  }
 
}