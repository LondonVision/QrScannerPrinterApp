var ss = SpreadsheetApp.openByUrl("https://docs.google.com/spreadsheets/d/1cBKjCVR9gEzZwBLdWo7XXPEpQZ9X8sK4HLqFUnB2wqE/edit#gid=0");

var sheet = ss.getSheetByName('StudentSignInOut')

function doPost(e){
  var action = e.parameter.action;
  if(action == "addStudent"){
    return addItem(e);
  }
}

function addItem(e){
var date = new Date;
var firstName = e.parameter.vFirstName;
var lastName = e.parameter.vLastName;
var email = e.parameter.vEmail;
var grade = e.parameter.vGrade
sheet.appendRow([date,firstName,lastName,email,grade]);
  return ContentService.createTextOutput("Success").setMimeType(ContentService.MimeType.TEXT);
}
