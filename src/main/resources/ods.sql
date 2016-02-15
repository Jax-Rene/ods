/*
 Navicat Premium Data Transfer

 Source Server         : localhost-postgresql
 Source Server Type    : PostgreSQL
 Source Server Version : 90404
 Source Host           : localhost
 Source Database       : ods
 Source Schema         : ods

 Target Server Type    : PostgreSQL
 Target Server Version : 90404
 File Encoding         : utf-8

 Date: 02/15/2016 23:33:41 PM
*/

-- ----------------------------
--  Sequence structure for findpassword_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "ods"."findpassword_id_seq";
CREATE SEQUENCE "ods"."findpassword_id_seq" INCREMENT 1 START 10 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "ods"."findpassword_id_seq" OWNER TO "postgres";

-- ----------------------------
--  Sequence structure for group_group_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "ods"."group_group_id_seq";
CREATE SEQUENCE "ods"."group_group_id_seq" INCREMENT 1 START 72 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "ods"."group_group_id_seq" OWNER TO "postgres";

-- ----------------------------
--  Sequence structure for message_message_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "ods"."message_message_id_seq";
CREATE SEQUENCE "ods"."message_message_id_seq" INCREMENT 1 START 221 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "ods"."message_message_id_seq" OWNER TO "admin";

-- ----------------------------
--  Sequence structure for order_order_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "ods"."order_order_id_seq";
CREATE SEQUENCE "ods"."order_order_id_seq" INCREMENT 1 START 40 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "ods"."order_order_id_seq" OWNER TO "admin";

-- ----------------------------
--  Sequence structure for order_user_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "ods"."order_user_id_seq";
CREATE SEQUENCE "ods"."order_user_id_seq" INCREMENT 1 START 101 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "ods"."order_user_id_seq" OWNER TO "admin";

-- ----------------------------
--  Sequence structure for test_register_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "ods"."test_register_id_seq";
CREATE SEQUENCE "ods"."test_register_id_seq" INCREMENT 1 START 23 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "ods"."test_register_id_seq" OWNER TO "admin";

-- ----------------------------
--  Sequence structure for user_user_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "ods"."user_user_id_seq";
CREATE SEQUENCE "ods"."user_user_id_seq" INCREMENT 1 START 18 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "ods"."user_user_id_seq" OWNER TO "admin";

-- ----------------------------
--  Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS "ods"."message";
CREATE TABLE "ods"."message" (
	"message_id" int4 NOT NULL DEFAULT nextval('message_message_id_seq'::regclass),
	"user_id" int4,
	"message_type" int4,
	"message_content" varchar(100) COLLATE "default",
	"message_time" timestamp(6) WITH TIME ZONE,
	"message_result" int4 DEFAULT 0,
	"message_group_id" int4 DEFAULT 0,
	"message_from" varchar(20) DEFAULT '系统'::character varying COLLATE "default",
	"message_from_id" int4 DEFAULT 0
)
WITH (OIDS=FALSE);
ALTER TABLE "ods"."message" OWNER TO "admin";

-- ----------------------------
--  Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS "ods"."order";
CREATE TABLE "ods"."order" (
	"order_id" int4 NOT NULL DEFAULT nextval('order_order_id_seq'::regclass),
	"order_type" int4,
	"order_time" timestamp(6) WITH TIME ZONE,
	"order_mark" varchar(100) DEFAULT '无'::character varying COLLATE "default",
	"order_group" int4,
	"order_price" float8 DEFAULT 0,
	"order_url" varchar(300) COLLATE "default",
	"order_end" timestamp(6) WITH TIME ZONE
)
WITH (OIDS=FALSE);
ALTER TABLE "ods"."order" OWNER TO "admin";

-- ----------------------------
--  Table structure for test_register
-- ----------------------------
DROP TABLE IF EXISTS "ods"."test_register";
CREATE TABLE "ods"."test_register" (
	"out_date" timestamp(6) WITH TIME ZONE,
	"user_name" varchar(20) COLLATE "default",
	"user_pass" varchar(14) COLLATE "default",
	"location" varchar(40) COLLATE "default",
	"valid_key" varchar(36) COLLATE "default",
	"id" int4 NOT NULL DEFAULT nextval('test_register_id_seq'::regclass)
)
WITH (OIDS=FALSE);
ALTER TABLE "ods"."test_register" OWNER TO "admin";

-- ----------------------------
--  Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS "ods"."user";
CREATE TABLE "ods"."user" (
	"user_name" varchar(20) COLLATE "default",
	"user_pass" varchar(14) COLLATE "default",
	"location" varchar(40) COLLATE "default",
	"user_id" int4 NOT NULL DEFAULT nextval('user_user_id_seq'::regclass)
)
WITH (OIDS=FALSE);
ALTER TABLE "ods"."user" OWNER TO "admin";

-- ----------------------------
--  Table structure for findpassword
-- ----------------------------
DROP TABLE IF EXISTS "ods"."findpassword";
CREATE TABLE "ods"."findpassword" (
	"id" int4 NOT NULL DEFAULT nextval('findpassword_id_seq'::regclass),
	"email" varchar(30) COLLATE "default",
	"out_date" timestamp(6) NULL,
	"valid_key" varchar(50) COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "ods"."findpassword" OWNER TO "postgres";

-- ----------------------------
--  Table structure for user_group
-- ----------------------------
DROP TABLE IF EXISTS "ods"."user_group";
CREATE TABLE "ods"."user_group" (
	"user_id" int4,
	"group_id" int4,
	"nick_name" varchar(20) COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "ods"."user_group" OWNER TO "postgres";

-- ----------------------------
--  Table structure for group
-- ----------------------------
DROP TABLE IF EXISTS "ods"."group";
CREATE TABLE "ods"."group" (
	"group_id" int4 NOT NULL DEFAULT nextval('group_group_id_seq'::regclass),
	"group_name" varchar(20) COLLATE "default",
	"group_boss_id" int4,
	"group_icon" varchar(150) DEFAULT 'defaulticon.jpg'::character varying COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "ods"."group" OWNER TO "postgres";

-- ----------------------------
--  Table structure for order_user
-- ----------------------------
DROP TABLE IF EXISTS "ods"."order_user";
CREATE TABLE "ods"."order_user" (
	"order_id" int4,
	"user_id" int4,
	"order_name" varchar(30) COLLATE "default",
	"order_number" int4,
	"order_price" float8,
	"id" int4 NOT NULL DEFAULT nextval('order_user_id_seq'::regclass),
	"nick_name" varchar(30) COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "ods"."order_user" OWNER TO "admin";


-- ----------------------------
--  Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "ods"."findpassword_id_seq" RESTART 11 OWNED BY "findpassword"."id";
ALTER SEQUENCE "ods"."group_group_id_seq" RESTART 73 OWNED BY "group"."group_id";
ALTER SEQUENCE "ods"."message_message_id_seq" RESTART 222 OWNED BY "message"."message_id";
ALTER SEQUENCE "ods"."order_order_id_seq" RESTART 41 OWNED BY "order"."order_id";
ALTER SEQUENCE "ods"."order_user_id_seq" RESTART 102 OWNED BY "order_user"."id";
ALTER SEQUENCE "ods"."test_register_id_seq" RESTART 24 OWNED BY "test_register"."id";
ALTER SEQUENCE "ods"."user_user_id_seq" RESTART 19 OWNED BY "user"."user_id";
-- ----------------------------
--  Primary key structure for table message
-- ----------------------------
ALTER TABLE "ods"."message" ADD PRIMARY KEY ("message_id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table order
-- ----------------------------
ALTER TABLE "ods"."order" ADD PRIMARY KEY ("order_id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table test_register
-- ----------------------------
ALTER TABLE "ods"."test_register" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table user
-- ----------------------------
ALTER TABLE "ods"."user" ADD PRIMARY KEY ("user_id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table group
-- ----------------------------
ALTER TABLE "ods"."group" ADD PRIMARY KEY ("group_id") NOT DEFERRABLE INITIALLY IMMEDIATE;

