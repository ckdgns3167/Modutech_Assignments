var express = require('express');
var dbRouter = require('./data/db.js');
var path = require('path');
var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.route('/api/folder').get(async (req, res) => {
  const result = { success: true }
  try {
    const json = await dbRouter.getData()
    result.data = json.folder
  } catch (err) {
    result.success = false;
    result.err = err
  }
  res.json(result)
});

app.use(express.json());
app.use(express.static(path.join(__dirname, 'public')));
app.listen(3000);


// catch 404 and forward to error handler
app.use(function (req, res, next) {
  next(createError(404));
});

// error handler
app.use(function (err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app; // app 객체를 모듈로 만듬.
