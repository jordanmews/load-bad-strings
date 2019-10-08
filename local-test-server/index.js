var cookieSession = require('cookie-session');
var express = require('express');
var https = require('https');
var fs = require('fs');
var express = require('express');
var bodyParser = require('body-parser');
var app = express();

// **** WHAT IS THIS ****
// A simple node-js server with http/https ports and mocks multiple functions like:
// - GET/POST, login, an incrementing view-count tied to server-side session,
//   cookies etc..

// **** HOW TO RUN ****
// cd local-test-server
// npm install
// node index.js

const httpPort = 3001;
const httpsPort = 3002; // beware the self-signed cert

app.set('trust proxy', 1);

// create application/json parser
var jsonParser = bodyParser.json({type: "*/*"});

// create application/x-www-form-urlencoded parser
var urlencodedParser = bodyParser.urlencoded({ extended: false });

// POST /login gets urlencoded bodies
app.post('/sign-in', urlencodedParser, function (req, res) {
    // more info: https://expressjs.com/en/resources/middleware/body-parser.html
    res.send('welcome, ' + req.body.username)
});

// POST /api/users gets JSON bodies
var users = []

app.post('/user', jsonParser, function (req, res) {
    // create user in req.body
    users.push(req.body);
    console.log(users);
    res.send('user created, ' + JSON.stringify(req.body))
});

app.use(cookieSession({
    name: 'session23',
    keys: ['key1', 'key2']
}));

app.get('/views', function (req, res, next) {
    req.session.views = (req.session.views || 0) + 1;
    res.end(req.session.views + ' views')

});
app.get('/users', function (req, res, next) {
    res.send(users);
});

// plaintext response
app.post('/hello', function (req, res, next) {
    res.send("hi")
});

// Json response
var rsp = {
    users: [
        {name: 'bob'},
        {name: 'alice'}
    ]
};

app.get('/json', function (req, res, next) {
    res.send(rsp)
});

// Listen on http and https ports
app.listen(httpPort);

https.createServer({
 //(Expect a self-signed cert warning from your browser)
 key: fs.readFileSync('server.key'),
 cert: fs.readFileSync('server.cert')
}, app)
 .listen(httpsPort);