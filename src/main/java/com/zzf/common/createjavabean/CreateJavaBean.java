package com.zzf.common.createjavabean;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.zzf.common.jdbc.JDBCDemo;

public class CreateJavaBean {
    public static final String DRIVER_CLASS = "org.mariadb.jdbc.Driver";

    public static final String DRIVER_URL = "jdbc:mysql://132.97.184.60:3306/yuwei_h5?characterEncoding=utf-8";

    public static final String USERNAME = "yuwei_h5";//dnxuser

    public static final String PASSWORD = "yuwei_h5";//domain

    public static final String DATABASE = "yuwei_h5";

    public static final String TABLENAME = "template_music";

    public static final String FILEPATH = "E://";

    public static final String PACKAGE = "com.yuwei.base.vo";

    public static boolean isAnnotation = true;

    /**
     * 
     * @return
     */
    public List<Columns> getColumnInfo() {
        List<Columns> list = new ArrayList<Columns>();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            conn = JDBCDemo.getConnection(DRIVER_CLASS, DRIVER_URL, USERNAME, PASSWORD);
            String sql = "select column_name,data_type,column_comment,column_key from information_schema.columns where table_name = '"
                    + TABLENAME + "' ";
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
            while (rs.next()) {
                Columns column = new Columns();
                column.setColumnName(rs.getString("column_name"));
                column.setFormatColumnName(formateColumnName(rs.getString("column_name")));
                column.setDataType(rs.getString("data_type"));
                column.setColumnComment(rs.getString("column_comment"));
                if ("PRI".equalsIgnoreCase(rs.getString("column_key"))) {
                    column.setKey(true);
                }
                list.add(column);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                rs.close();
                stat.close();
                conn.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 
     * @param columnName
     * @return
     */
    private String formateColumnName(String columnName) {
        int index = columnName.indexOf("_");
        if (index > -1) {
            columnName = columnName.substring(0, index)
                    + getFirstUpper(columnName.substring(index + 1, columnName.length()));
        }
        return columnName;
    }

    /**
     * 
     * @param str
     * @return
     */
    private String getFirstUpper(String str) {
        return str.replaceFirst(str.substring(0, 1), str.substring(0, 1).toUpperCase());
    }

    /**
     * 
     * @param bw
     * @param list
     * @throws IOException
     */
    private void writeImport(BufferedWriter bw, List<Columns> list) throws IOException {
        boolean isdate = false;
        for (Columns column : list) {
            if (!isdate
                    && ("datetime".equalsIgnoreCase(column.getDataType()) || "TIMESTAMP".equalsIgnoreCase(column
                            .getDataType()))) {
                bw.write("import java.util.Date;");
                bw.newLine();
                isdate = true;
            }
        }
        if (isAnnotation) {
            bw.write("import static javax.persistence.GenerationType.IDENTITY;");
            bw.newLine();
            bw.write("import javax.persistence.Column;");
            bw.newLine();
            bw.write("import javax.persistence.Entity;");
            bw.newLine();
            bw.write("import javax.persistence.GeneratedValue;");
            bw.newLine();
            bw.write("import javax.persistence.Id;");
            bw.newLine();
            bw.write("import javax.persistence.Table;");
            bw.newLine();
        }
    }

    private String getType(String dataType) {
        if (("datetime".equalsIgnoreCase(dataType) || "TIMESTAMP".equalsIgnoreCase(dataType))) {
            return "Date";
        }
        else if ("bigint".equalsIgnoreCase(dataType)) {
            return "Long";
        }
        else if ("int".equalsIgnoreCase(dataType)) {
            return "Integer";
        }
        else if ("double".equalsIgnoreCase(dataType)) {
            return "Double";
        }
        else {
            return "String";
        }
    }

    private void writeFiled(BufferedWriter bw, List<Columns> list) throws IOException {
        for (Columns column : list) {
            String type = getType(column.getDataType());
            //System.out.println(StringUtil.getCharsetName(column.getColumnComment()));
            bw.write("    /** " + column.getColumnComment() + " */");
            bw.newLine();
            bw.write("    private " + type + " " + column.getFormatColumnName() + ";");
            bw.newLine();
            bw.newLine();
        }
    }

    private void writeGetAndSetMethod(BufferedWriter bw, List<Columns> list) throws IOException {
        for (Columns column : list) {
            String type = getType(column.getDataType());
            if (isAnnotation) {
                if (column.isKey()) {
                    bw.write("    @Id");
                    bw.newLine();
                    bw.write("    @GeneratedValue(strategy = IDENTITY)");
                    bw.newLine();
                    bw.write("    @Column(name = \"" + column.getColumnName() + "\", unique = true, nullable = false)");
                    bw.newLine();
                }
                else {
                    bw.write("    @Column(name = \"" + column.getColumnName() + "\")");
                    bw.newLine();
                }
            }
            bw.write("    public " + type + " get" + getFirstUpper(column.getFormatColumnName()) + "() { ");
            bw.newLine();
            bw.write("        return this." + column.getFormatColumnName() + "; ");
            bw.newLine();
            bw.write("    } ");
            bw.newLine();
            bw.newLine();
            bw.write("    public void set" + getFirstUpper(column.getFormatColumnName()) + "(" + type + " "
                    + column.getFormatColumnName() + ") { ");
            bw.newLine();
            bw.write("        this." + column.getFormatColumnName() + " = " + column.getFormatColumnName() + "; ");
            bw.newLine();
            bw.write("    } ");
            bw.newLine();
            bw.newLine();
        }
    }

    /**
     * 
     */
    public void writer(List<Columns> list) {
        try {
            String name = getFirstUpper(TABLENAME);
            File file = new File(FILEPATH + name + ".java");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write("package " + PACKAGE + ";");
            bw.newLine();
            bw.newLine();
            writeImport(bw, list);
            bw.newLine();
            bw.newLine();
            if (isAnnotation) {
                bw.write("@Entity");
                bw.newLine();
                bw.write("@Table(name = \"" + TABLENAME + "\", catalog = \"" + DATABASE + "\")");
                bw.newLine();
            }
            bw.write("public class " + name + " implements java.io.Serializable {");
            bw.newLine();
            bw.newLine();
            writeFiled(bw, list);
            bw.newLine();
            writeGetAndSetMethod(bw, list);
            bw.write("}");
            bw.close();
            System.out.println("execute end ........");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CreateJavaBean bean = new CreateJavaBean();
        bean.writer(bean.getColumnInfo());
    }

}
