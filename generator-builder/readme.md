1.启动：
    com.casic.cloud.hyperloop包下Generator类，main方法启动。
2.参数配置：
    com.casic.cloud.hyperloop.Generator 可配参数
        a.buildPath 文件生成绝对路径
        b.tableName *或者表名，"*"代表生成所有表，"表名"生成但张表
        c.userName 注释中@author的值
    generator.xml 可配参数
        a.basepackage生成实体的包路径，例：com.casic.cloud.hyperloop
        b.tableRemovePrefixes表名移除前缀，例：d_
        c.jdbc.username、jdbc.password、jdbc.url数据库链接信息配置
        