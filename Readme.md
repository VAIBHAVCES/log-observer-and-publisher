## Problem

* Idea is to learn about web sockets and stomp protocol via this minor project
*  So problem for this project is, you have to make log streaming service which can stream logs as much live as possible
*  This java application will be deployed on server and it should allow user to access a page where logs could be seen streaming without a reload

## Solution
* Logs are a very accurate entity to capture so my idea was to use any streaming based protocol for this purpouse
* And thats why I preffered to use STOMP connection based communication
* Where user will be able to pass the path on the server via an HTTP REST API
* Using that API we will start monitoring the logs and will notify on a public channel
* Note that when user opens the screen it should also be  able to see the last 10 lines of file

## My solution
* Provision an http rest api `/set-path`
* This API I will use to set up a thread in the backend server which will initate a continous montioring thread in the backend which will monitor the file and will publish it to a  **public topic**
* Now as the users will connect first via frontend to my web socket connection API to the backend `/ws`
* Frontend have to request inital set of logs via `/init-logs` which publish the last 10lines of text to that user queue
* User will display them on the page and then can `unsubscribe` to this queue
* And user can  subscribe to the public topic  `/topic/updates` where we will stream the latest appended statements
* Note that I am maintaining a single thread executor service that will make sure that at one time I am streaming only a single file
* And also this monitoring service is using `java.nio` based `file-channels` which don't have to load whole file in memory and can save us a lot of space in tracking any new changes and sending them to user

## How to run
* start the backend via running the main java code
* trigger the frontend by putting `npm start` in the frontend directory
* Run 'set-path' api with following content body'{content: < PATH >}'