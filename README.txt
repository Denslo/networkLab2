Class description:

Reminder - an Object representing a reminder in the system
Task - an Object representing a task in the system (extend Reminder)
Polls - an Object representing a poll in the system(extend Task)
Recipient - an Object representing a recipient for a task or a poll
Answer - an Object representing an answer for a poll

Request - an object representing a parsed HTTP request
Response - an object representing a parsed HTTP response

HttpRequestQueue - a queue for handling and maintaining all the HTTP request the server handle
SMTPNotifictionThread - a thread that read the DB once every minute and decide if an action should take place. (send mail, close poll ...) this thread also update the DB of its actions.

DBHandler - a class with only static methods that handles all the writing and reading from and to the DB
Helper - a class with only static methods that contains methods that are used by several unrelated class
SMTPClient - a class that execute the sending of a mail via SMTP protocol

HttpRequest - a class that handle the HTTP request that the server received 
SMTPRequest - a class the handle requests that refer to the SMTP client on the server

Server - the main class of this server. simply start the leasing process load config.ini.

Server logic:

on startup the server starts from Server class (where our main is written). on its startup its load the config.ini,
start the SMTPNotifictionThread, initialize a HttpRequestQueue and wait for HTTP request.

the SMTPNotifictionThread check the DB every minute on the minute and if it is necessary it send an email using the SMTPClient class
and update the DB using the DBHandler class if so.

every request to our server is beaning handled as requested in lab 1 and if the request is for our SMTP client application
the HttpRequest use the SMTPRequest class witch handle all the requests as requested in lab 2. this way our SMTP client application
can answer all the requirements of this lab (handle cookie and so on) but the rest of the server (all files and folders under rootserver)
will stile be handled as requested on lab 1.

for the DB we have chosen XML format since it is simple and do not require an actual DB