CREATE USER debezium WITH LOGIN REPLICATION PASSWORD 'debezium';

create schema example;

DROP TABLE IF EXISTS public.lab_work;
DROP TABLE IF EXISTS public.mark;
DROP TABLE IF EXISTS public.usr;
DROP TABLE IF EXISTS public.student;


create table if not exists public.lab_work (
                                        lab_num int4 not null,
                                        primary key (lab_num)
);

create table if not exists public.mark (
                                    value int4 not null,
                                    student_id int8 not null,
                                    lab_lab_num int4 not null,
                                    primary key (lab_lab_num, student_id)
);

create table if not exists public.student (
                                       id bigserial not null,
                                       name varchar(255),
                                       primary key (id)
);

create table if not exists public.usr (
                                   user_id bigserial,
                                   active boolean,
                                   pass_hash varchar(255) not null,
                                   roles varchar(255),
                                   username varchar(255) not null,
                                   primary key (user_id)
);

alter table if exists public.mark
    add constraint FKbgcjdd8log9hbetxp71u3v9y
        foreign key (student_id)
            references public.student;

alter table if exists public.mark
    add constraint FKlfv2hftt06qqiioagbplm9cjb
        foreign key (lab_lab_num)
            references public.lab_work;

INSERT INTO public.lab_work VALUES (1), (2), (3), (4);

INSERT INTO public.student VALUES (10, 'Карпов Н.И.'), (11, 'Биглер П.П.'), (12, 'Куконен Е.И.');

INSERT INTO public.mark VALUES ( 7, 10, 1), ( 9, 11, 1), ( 8, 12, 1), ( 0, 10, 2),
                        ( 0, 11, 2), ( 0, 12, 2), ( 0, 10, 3), ( 0, 11, 3),
                        ( 0, 12, 3), ( 0, 10, 4), ( 0, 11, 4), ( 0, 12, 4);

INSERT INTO public.usr(user_id, username, pass_hash, active, roles) VALUES (1, 'admin1', '$2a$10$nI/mVcA2sHAc.VPIbHfe0Ohf6.FuoVVjZcp/c0dXJDPQpjGv0TCwa', true, 'ROLE_ADMIN'),
                                                                    (2, 'admin2', '$2a$10$GZdjUEG5RtYOKcCC47/XVOD4B4UBcZU.VkOoxdjX2aSdRkpa081Jm', true, 'ROLE_ADMIN'),
                                                                    (3, 'user1', '$2a$10$S.O6UGn6AsbhlEzJ.DNYkerWF/z0iwQg46S.2ybxxlbZhI712DruG', true, 'ROLE_USER');
ALTER SCHEMA example OWNER TO debezium;