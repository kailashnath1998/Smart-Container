var { ObjectID } = require('mongodb');
var { mongoose } = require("./../db/mongoose.js");
var { Limits } = require("./../models/limits");
var moment = require('moment');

async function asyncForEach(array, callback) {
  for (let index = 0; index < array.length; index++) {
    await callback(array[index], index, array);
  }
}

var setLimits = async (obj) => {

  var date = moment();
  var dat = date.format('MMM Do, YYYY');
  var time = date.format('HH:mm:ss');
  var json = {
    "value": obj.value,
    "parameter": obj.parameter,
    "capture_date": dat,
    "capture_time": time
  }
  var flag = 0;
  //var ret = await Limits.findOneAndUpdate({}, { $set: newVal });
  var ret = await Limits.find({parameter : obj.parameter}).then(async (ob) => {
     if(!ob) {
        await Limits.findOneAndUpdate({parameter : obj.parameter}, {$set : json});
     }
     else {
        await Limits.create(json);
     }

  });


  return ret;
}


module.exports = { setLimits };
