package com.mailian.generator.util;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/22
 * @Description:
 */
public class InsertBatchElementGenerator extends AbstractXmlElementGenerator {

    public InsertBatchElementGenerator() {
    }

    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("insert");
        answer.addAttribute(new Attribute("id", "insertBatch"));
        answer.addAttribute(new Attribute("parameterType", "java.util.List"));
        this.context.getCommentGenerator().addComment(answer);
        StringBuilder insertClause = new StringBuilder();
        StringBuilder valuesClause = new StringBuilder();
        insertClause.append("insert into ");
        insertClause.append(this.introspectedTable.getFullyQualifiedTableNameAtRuntime());
        insertClause.append(" (");
        //answer.addElement(new TextElement(insertClause.toString()));

        XmlElement foreachElement = new XmlElement("foreach");
        foreachElement.addAttribute(new Attribute("collection","objs"));
        foreachElement.addAttribute(new Attribute("item","item"));
        foreachElement.addAttribute(new Attribute("index","index"));
        foreachElement.addAttribute(new Attribute("separator",","));

        List<String> valuesClauses = new ArrayList();
        List<IntrospectedColumn> columns = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(this.introspectedTable.getAllColumns());

        valuesClause.append(" (");
        for(int i = 0; i < columns.size(); ++i) {
            IntrospectedColumn introspectedColumn = (IntrospectedColumn)columns.get(i);
            insertClause.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            valuesClause.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn,"item."));
            if (i + 1 < columns.size()) {
                insertClause.append(", ");
                valuesClause.append(", ");
            }

            if (valuesClause.length() > 80) {
                answer.addElement(new TextElement(insertClause.toString()));
                insertClause.setLength(0);
                OutputUtilities.xmlIndent(insertClause, 1);
                valuesClauses.add(valuesClause.toString());
                valuesClause.setLength(0);
                OutputUtilities.xmlIndent(valuesClause, 1);
            }
        }

        valuesClause.append(")");
        insertClause.append(')');
        insertClause.append(" values ");
        answer.addElement(new TextElement(insertClause.toString()));
        valuesClauses.add(valuesClause.toString());
        Iterator var12 = valuesClauses.iterator();

        while(var12.hasNext()) {
            String clause = (String)var12.next();
            foreachElement.addElement(new TextElement(clause));
        }
        answer.addElement(foreachElement);

        if (this.context.getPlugins().sqlMapInsertElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }

    }
}
