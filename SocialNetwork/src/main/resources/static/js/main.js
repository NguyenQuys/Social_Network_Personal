// 'use strict';

// var usernamePage = document.querySelector('#username-page');
// var chatPage = document.querySelector('#chat-page');
// var usernameForm = document.querySelector('#usernameForm');
// var messageForm = document.querySelector('#messageForm');
// var messageInput = document.querySelector('#message');
// var messageArea = document.querySelector('#messageArea');
// var connectingElement = document.querySelector('.connecting');

// var stompClient = null;
// var username = null;

// var colors = [
//     '#2196F3', '#32c787', '#00BCD4', '#ff5652',
//     '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
// ];

// usernameForm.addEventListener('submit', connect, true);

// document.addEventListener('DOMContentLoaded', function() {
//     document.querySelectorAll('.clickable-link').forEach(function(link) {
//         link.addEventListener('click', function(event) {
//             event.preventDefault();

//             var data1 = this.getAttribute('data-username');
//             var data2 = this.getAttribute('data-avatar');

//             sendDataToDiv2(data1, data2);
//             connect(event, data1); // Pass the username to the connect function
//         });
//     });
// });

// function sendDataToDiv2(data1, data2) {
//     document.getElementById('targetData1').textContent = data1;
//     document.getElementById('targetImage').src = data2;
// }

// function connect(event, username) {
//     event.preventDefault(); // Prevent the form from submitting normally
//     console.log('Form submitted'); // Check if the submit event is triggered

//     console.log('Username:', username); // Check the value of the username

//     if (username) {
//         usernamePage.classList.add('hidden');
//         chatPage.classList.remove('hidden');
//         console.log('Username page hidden, chat page shown'); // Check if the form is hidden and chat page is shown

//         var socket = new SockJS('/ws');
//         stompClient = Stomp.over(socket);

//         stompClient.connect({}, onConnected, onError);
//         console.log('WebSocket connection attempted'); // Check WebSocket connection

//         // Fetch chat history
//         fetch('/history?username=' + username)
//             .then(response => response.json())
//             .then(messages => {
//                 messages.forEach(message => {
//                     displayMessage(message);
//                 });
//             });
//     }
// }

// function displayMessage(message) {
//     var messageElement = document.createElement('li');

//     if (message.sender === '[[${currentlyUser.username}]]') {
//         messageElement.classList.add('chat-message', 'text-author');
//     } else {
//         messageElement.classList.add('text-friends');
//     }

//     var textElement = document.createElement('p');
//     var messageText = document.createTextNode(message.content);
//     textElement.appendChild(messageText);

//     messageElement.appendChild(textElement);

//     // Add the message time
//     var messageTime = document.createElement('div');
//     messageTime.classList.add('message-time');
//     var timeText = document.createTextNode(new Date(message.timestamp).toLocaleTimeString());
//     messageTime.appendChild(timeText);
//     messageElement.appendChild(messageTime);

//     messageArea.appendChild(messageElement);
//     messageArea.scrollTop = messageArea.scrollHeight;
// }

// function onConnected() {
//     console.log('Connected to WebSocket'); // Check if connected to WebSocket
//     // Subscribe to the Public Topic
//     stompClient.subscribe('/topic/public', onMessageReceived);

//     // Tell your username to the server
//     stompClient.send("/app/chat.addUser",
//         {},
//         JSON.stringify({ sender: username, type: 'JOIN' })
//     )

//     connectingElement.classList.add('hidden');
// }

// function onError(error) {
//     console.error('WebSocket connection error:', error); // Display the error if any
//     connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
//     connectingElement.style.color = 'red';
// }

// function sendMessage(event) {
//     var messageContent = messageInput.value.trim();
//     if (messageContent && stompClient) {
//         var chatMessage = {
//             recipient: username,
//             sender: '[[${currentlyUser.username}]]',
//             content: messageInput.value,
//             type: 'CHAT'
//         };
//         stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
//         messageInput.value = '';
//     }
//     event.preventDefault();
// }

// function onMessageReceived(payload) {
//     var message = JSON.parse(payload.body);

//     if (message.type !== 'CHAT') {
//         return; // Skip if it's not a CHAT message
//     }

//     var messageElement = document.createElement('li');

//     if (message.sender !== username) {
//         messageElement.classList.add('chat-message', 'text-author');
//     } else {
//         messageElement.classList.add('text-friends');
//     }

//     var textElement = document.createElement('p');
//     var messageText = document.createTextNode(message.content);
//     textElement.appendChild(messageText);

//     messageElement.appendChild(textElement);

//     // Add the message time
//     var messageTime = document.createElement('div');
//     messageTime.classList.add('message-time');
//     var timeText = document.createTextNode(new Date().toLocaleTimeString());
//     messageTime.appendChild(timeText);
//     messageElement.appendChild(messageTime);

//     messageArea.appendChild(messageElement);
//     messageArea.scrollTop = messageArea.scrollHeight;
// }

// function getAvatarColor(messageSender) {
//     var hash = 0;
//     for (var i = 0; i < messageSender.length; i++) {
//         hash = 31 * hash + messageSender.charCodeAt(i);
//     }
//     var index = Math.abs(hash % colors.length);
//     return colors[index];
// }

// document.querySelectorAll('.clickable-link').forEach(function (link) {
//     link.addEventListener('click', function (event) {
//         event.preventDefault();

//         var data1 = this.getAttribute('data-username');
//         var data2 = this.getAttribute('data-avatar');

//         sendDataToDiv2(data1, data2);
//     });
// });

// function sendDataToDiv2(data1, data2) {
//     document.getElementById('targetData1').textContent = data1;
//     document.getElementById('targetImage').src = data2;
// }

// usernameForm.addEventListener('submit', connect, true);
// messageForm.addEventListener('submit', sendMessage, true);

// // Listen for keypress events to catch the Enter key in the textarea
// messageInput.addEventListener('keypress', function (event) {
//     if (event.key === 'Enter' && !event.shiftKey) {
//         sendMessage(event);
//     }
// });