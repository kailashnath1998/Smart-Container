var express = require('express');
var bodyParser = require('body-parser');
var { addSensor } = require("./utility/addSensor.js");
var { addToBlockChain } = require("./utility/addToBlockChain.js");
var { Sensor } = require('./models/sensor');
var path = require("path");


var cors = require('cors');
var app = express();
app.use(cors());


app.get('/', function (req, res) {
    res.send("I'm listening");
});


app.use(bodyParser.urlencoded({ extended: false }))
app.use(bodyParser.json())


app.post('/sensorValues', function (req, res) {
    var data = req.body.data;
    var obj = {};

    data.forEach(element => {
        var tmp = {};
        tmp.parameter = element.parameter;
        tmp.value = element.value;
        addSensor(tmp).then((ob) => {
            console.log(ob);
            addToBlockChain(ob).then((kk) => {
                console.log("\nAdded\n");
                //res.status(200).send();
            }).catch((err) => {
                res.status(400).json({ err: err.message });
            });

        }).catch((err) => {
            res.status(400).json({ err: err.message });
        });
    });

    res.status(200).send();

});


app.get('/sensordata/:parameter', function (req, res) {

    Sensor.find().then((obj) => {
        var keys = [];
        for (let i = 0; i < obj.length; i++) {
            //console.log(obj[i]);
            var st = req.params.parameter;
            if (obj[i].parameter === st) {
                var tmp = {};
                var time = [];
                time = obj[i].capture_time.split(":").map(Number);;
                tmp.time = time;
                tmp.value = obj[i].value;
                keys.push(tmp);
            }

        }
        var ret = { "keys": keys };
        res.json(ret);
    });
});


app.get("/visualize/sensor/1", function (req, res) {
    res.sendFile(path.join(__dirname + "/../dashboard/sensor1.html"));
});

let ct = "5";

app.get("/set", function (req, res) {
    ct = req.query.val;
    res.send("Changed Successfully");
});

app.get("/count", function (req, res) {
    res.send(ct);
});

const PORT = 3000;
const HOST = '0.0.0.0';


var server = app.listen(3000, function () {
    console.log("running");
});
//console.log('Running on http://localhost:3000');