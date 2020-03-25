package com.casic.cloud.hyperloop.autocoding;

import com.casic.cloud.hyperloop.autocoding.common.Column;
import com.casic.cloud.hyperloop.autocoding.common.Table;
import com.casic.cloud.hyperloop.autocoding.utils.CamelCaseUtils;
import com.casic.cloud.hyperloop.autocoding.utils.FileHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lvbaolin
 */
public class Generator {
    private Properties properties;
    private SimpleDateFormat sm_date = new SimpleDateFormat("yyyy年MM月dd日");
    private SimpleDateFormat sm_year = new SimpleDateFormat("yyyy年");
    private Connection conn;
    private DatabaseMetaData dmd;
    String catalog;
    String schema;


    /**
     * 构造方法
     * 1.初始化property
     * 2.初始化connection
     * 3.初始化DatabaseMetaData
     *
     * @throws Exception
     */
    public Generator() throws Exception {
        properties = new Properties();
        String fileDir = this.getClass().getClassLoader().getResource("generator.xml").getFile();
        properties.loadFromXML(new FileInputStream(fileDir));
        catalog = properties.getProperty("jdbc.catalog");
        schema = properties.getProperty("jdbc.schema");
        //加载驱动
        String driverName = properties.getProperty("jdbc.driver");
        Class.forName(driverName);
        Properties prop = new Properties();
        prop.setProperty("user", properties.getProperty("jdbc.username"));
        prop.setProperty("password", properties.getProperty("jdbc.password"));
        prop.setProperty("remarks", "true");
        prop.setProperty("useInformationSchema", "true");
        //创建连接
        conn = java.sql.DriverManager.getConnection(properties.getProperty("jdbc.url"), prop);
        //获取databaseMetaData
        dmd = conn.getMetaData();

    }

    /**
     *  文件生成路径
     */
    private String buildPath = "/Users/lvbaolin/work706/cloud_govern/generator-builder/src/main/java/com/casic/cloud/hyperloop";
    /**
     * 表名单表生成传入表名,多表生成传入*
     */
    private static String tableName = "oauth_client_details";
    private static String userName = "lvbl";

    public static void main(String[] args) throws Exception {
                Generator generator = new Generator();
        generator.gen(tableName, "", userName);
        System.out.println("模版文件生成完毕……");
    }


    /**
     * <p>Discription:[生成映射文件和实体类]</p>
     *
     * @param tableName 要声称映射文件和实体类的表名称
     * @throws Exception
     */
    public void gen(String tableName, String model_package_name, String uname) {

        try {
            if ("*".equals(tableName)) {
                ResultSet tableSet = dmd.getTables(catalog, schema, null, null);
                while (tableSet.next()) {
                    System.out.println(tableSet.getString("TABLE_NAME"));
                    tableName = tableSet.getString("TABLE_NAME");
                    buildFile(parseTable(tableName), model_package_name, uname);
                }
            } else {
                buildFile(parseTable(tableName), model_package_name, uname);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    void buildFile(Table t, String model_package_name, String uname) throws Exception {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_21);
        //封装template需要的数据
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("table", t);
        root.put("uname", uname);
        root.put("className", t.getNameUpper());
        root.put("classNameLower", t.getName());
        root.put("package", properties.getProperty("basepackage"));
        root.put("model_package_name", model_package_name);
        root.put("date", sm_date.format(new Date()));
        root.put("year", sm_year.format(new Date()));
        String templateDir = this.getClass().getClassLoader().getResource("templates").getPath();

        File tdf = new File(templateDir);
        List<File> files = FileHelper.findAllFile(tdf);

        for (File f : files) {
            String parentDir = "";
            if (f.getParentFile().compareTo(tdf) != 0) {
                parentDir = f.getParent().split("templates")[1];
            }
            cfg.setClassForTemplateLoading(this.getClass(), "/templates" + parentDir);
            Template template = cfg.getTemplate(f.getName());
            template.setEncoding("UTF-8");

            String parentFileDir = FileHelper.genFileDir(parentDir, root);
            parentFileDir = parentFileDir.replace(".", "/");
            String file = FileHelper.genFileDir(f.getName(), root).replace(".ftl", ".java");


            File newFile = FileHelper.makeFile(buildPath + parentFileDir + "/" + file);
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile), "UTF-8"));
            template.process(root, out);

        }
    }


    /**
     * 读取数据库表信息
     *
     * @param tableName 表名
     * @return
     */
    public Table parseTable(String tableName) throws Exception {
        Table t = new Table();

        ResultSet columnsResultSet = dmd.getColumns(catalog, schema == null ? "%" : schema, tableName, "%");
        List<Column> columnList = new ArrayList<Column>();
        List<String> columnTypeList=new ArrayList<>();
        while (columnsResultSet.next()) {
            Column c = new Column();
            c.setLabel(columnsResultSet.getString("REMARKS"));
            c.setName(CamelCaseUtils.toCamelCase(columnsResultSet.getString("COLUMN_NAME")));
            c.setDbName(columnsResultSet.getString("COLUMN_NAME"));
            String dbType = columnsResultSet.getString("TYPE_NAME");
            c.setDbType(dbType);
            c.setType(properties.getProperty(dbType) == null ? "String" : properties.getProperty(dbType));
            c.setLength(columnsResultSet.getInt("COLUMN_SIZE"));
            c.setDecimalDigits(columnsResultSet.getInt("DECIMAL_DIGITS"));
            c.setNullable(columnsResultSet.getBoolean("NULLABLE"));
            switch (c.getType()){
                case "BigDecimal" :
                    columnTypeList.add("java.math.BigDecimal");
                    break;
                case "Date":
                    columnTypeList.add("java.util.Date");
                    break;
                default:;
            }
            columnList.add(c);
        }
        //集合去重复
        columnTypeList.stream().distinct().collect(Collectors.toList());
        t.setColumnTypeList(columnTypeList);
        List<Column> pkColumnList = new ArrayList<Column>();
        ResultSet pkColumnResultSet = dmd.getPrimaryKeys(catalog, schema, tableName);
        while (pkColumnResultSet.next()) {
            Column c = new Column();
            String name = pkColumnResultSet.getString("COLUMN_NAME");
            c.setName(CamelCaseUtils.toCamelCase(name));
            c.setDbName(name);
            pkColumnList.add(c);
        }

        ResultSet tableSet = dmd.getTables(catalog, schema, tableName, null);
        while (tableSet.next()) {
            t.setClassRemark(tableSet.getString("REMARKS"));
        }

        t.setName(getEntityName(tableName));
        t.setDbName(tableName);
        t.setColumns(columnList);
        t.setPkColumns(pkColumnList);


        return t;
    }


    /**
     * 获取实体名称
     *
     * @param tableName 表名
     * @return
     */
    String getEntityName(String tableName) {
        String prefiex = properties.getProperty("tableRemovePrefixes");
        String name = tableName;
        if (prefiex != null && !"".equals(prefiex)) {
            name = tableName.replace(prefiex, "");
        }
        return CamelCaseUtils.toCamelCase(name);
    }


}
