create table journal (
  id        integer primary key autoincrement,
  request_id varchar unique not null,
  create_date timestamp not null,
  body varchar not null
)