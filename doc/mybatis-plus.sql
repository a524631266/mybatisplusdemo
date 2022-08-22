use mybatis;

create table tbl_employee(
    id int(11) primary key auto_increment,
    last_name varchar(50),
    email varchar(50),
    gender char(1),
    age int
);

insert into tbl_employee (last_name, email, gender, age) values ('Tom', 'tom@agg.com', 1, 40);
insert into tbl_employee (last_name, email, gender, age) values ('Jerry', 'Jerry@agg.com', 1, 35);
insert into tbl_employee (last_name, email, gender, age) values ('Black', 'Black@agg.com', 1, 12);
insert into tbl_employee (last_name, email, gender, age) values ('Black', 'Black@agg.com', 1, 35);

select * from tbl_employee;

# 使用 mybatisX generator


drop procedure if exists test1;
delimiter //
create procedure test1(in ids int)
begin
    select * from tbl_employee where id = ids;
end //;
call test1(1);