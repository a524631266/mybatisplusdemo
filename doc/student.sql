use mybatis;

select * from t_student;

drop table t_student;
create table t_student
(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `cnname` varchar(50) NOT NULL COMMENT '中文名',
    `note` varchar(50) NOT NULL COMMENT '备注',
    `sex` tinyint(4) NOT NULL default 1 COMMENT '''2:女 1：男''' ,
    `is_delete` tinyint(4) NOT NULL COMMENT '''0:未删除 1：已删除''',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='学生表';

create table t_student_selfcard
(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `student_id` bigint(20) NOT NULL  COMMENT 'student表中的id',
    `native` varchar(50) NOT NULL COMMENT '籍贯',
    `issue_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '发卡时间',
    `end_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '无效时间',
    `note` varchar(50) NOT NULL COMMENT '备注',
    `is_delete` tinyint(4) NOT NULL COMMENT '''0:未删除 1：已删除''',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='学生证表';


select * from t_student_selfcard as a
left join t_student ts
on a.student_id = ts.id
where ts.id = 1;





