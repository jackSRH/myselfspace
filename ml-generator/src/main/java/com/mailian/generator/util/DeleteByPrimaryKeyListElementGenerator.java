package com.mailian.generator.util;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.Iterator;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/30
 * @Description:
 */
public class DeleteByPrimaryKeyListElementGenerator extends AbstractXmlElementGenerator {
    public DeleteByPrimaryKeyListElementGenerator() {
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("delete");
        answer.addAttribute(new Attribute("id", "deleteBatchIds"));
        answer.addAttribute(new Attribute("parameterType", "java.util.List"));
        this.context.getCommentGenerator().addComment(answer);
        StringBuilder sb = new StringBuilder();
        sb.append("delete from ");
        sb.append(this.introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        boolean and = false;
        Iterator var6 = this.introspectedTable.getPrimaryKeyColumns().iterator();

        while(var6.hasNext()) {
            IntrospectedColumn introspectedColumn = (IntrospectedColumn)var6.next();
            sb.setLength(0);
            if (and) {
                sb.append("  and ");
            } else {
                sb.append("where ");
                and = true;
            }
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" in ");
            answer.addElement(new TextElement(sb.toString()));

            XmlElement foreachElement = new XmlElement("foreach");
            foreachElement.addAttribute(new Attribute("collection","ids"));
            foreachElement.addAttribute(new Attribute("item","item"));
            foreachElement.addAttribute(new Attribute("index","i"));
            foreachElement.addAttribute(new Attribute("open","("));
            foreachElement.addAttribute(new Attribute("close",")"));
            foreachElement.addAttribute(new Attribute("separator",","));
            sb.setLength(0);
            sb.append(ParameterUtil.getParameterClause("item",introspectedColumn));
            foreachElement.addElement(new TextElement(sb.toString()));
            answer.addElement(foreachElement);
        }

        if (this.context.getPlugins().sqlMapDeleteByPrimaryKeyElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
