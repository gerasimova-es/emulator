create table journal (
  id        integer primary key autoincrement,
  request_id varchar unique,
  create_date timestamp,
  body varchar
)