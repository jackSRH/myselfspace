package com.mailian.generator.util;

import cn.hutool.core.collection.CollectionUtil;
import com.mailian.core.util.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/27
 * @Description:
 */
public class SelectListElementGenerator extends AbstractXmlElementGenerator {

    public SelectListElementGenerator() {
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute("id", "selectByMap"));
        if (this.introspectedTable.getRules().generateResultMapWithBLOBs()) {
            answer.addAttribute(new Attribute("resultMap", this.introspectedTable.getResultMapWithBLOBsId()));
        } else {
            answer.addAttribute(new Attribute("resultMap", this.introspectedTable.getBaseResultMapId()));
        }

        answer.addAttribute(new Attribute("parameterType", "map"));
        this.context.getCommentGenerator().addComment(answer);
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        if (StringUtility.stringHasValue(this.introspectedTable.getSelectByPrimaryKeyQueryId())) {
            sb.append('\'');
            sb.append(this.introspectedTable.getSelectByPrimaryKeyQueryId());
            sb.append("' as QUERYID,");
        }

        answer.addElement(new TextElement(sb.toString()));
        answer.addElement(this.getBaseColumnListElement());
        if (this.introspectedTable.hasBLOBColumns()) {
            answer.addElement(new TextElement(","));
            answer.addElement(this.getBlobColumnListElement());
        }

        sb.setLength(0);
        sb.append("from ");
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement dynamicElement = new XmlElement("where");
        answer.addElement(dynamicElement);
        Iterator var6 = ListUtilities.removeGeneratedAlwaysColumns(this.introspectedTable.getNonPrimaryKeyColumns()).iterator();

        String likeNames = introspectedTable.getTableConfigurationProperty("likeNames");
        List<String> likeNameList = new ArrayList<>();
        if(StringUtils.isNotEmpty(likeNames)){
            CollectionUtil.addAll(likeNameList,likeNames.split(","));
        }

        while(var6.hasNext()) {
            IntrospectedColumn introspectedColumn = (IntrospectedColumn)var6.next();
            String mybatisColumnName = MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn);
            if(likeNameList.contains(mybatisColumnName)){
                XmlElement likeElement = new XmlElement("if");
                sb.setLength(0);
                sb.append(introspectedColumn.getJavaProperty());
                sb.append(" != null");
                likeElement.addAttribute(new Attribute("test", sb.toString()));
                dynamicElement.addElement(likeElement);
                sb.setLength(0);
                sb.append(" and ");
                sb.append(mybatisColumnName);
                //and site_name like concat('%', #{siteName,jdbcType=VARCHAR}, '%')
                sb.append(" like concat('%',");
                sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
                sb.append(",'%') ");
                likeElement.addElement(new TextElement(sb.toString()));
            }else{
                XmlElement isNotNullElement = new XmlElement("if");
                sb.setLength(0);
                sb.append(introspectedColumn.getJavaProperty());
                sb.append(" != null");
                isNotNullElement.addAttribute(new Attribute("test", sb.toString()));
                dynamicElement.addElement(isNotNullElement);
                sb.setLength(0);
                sb.append(" and ");
                sb.append(mybatisColumnName);
                sb.append(" = ");
                sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
                isNotNullElement.addElement(new TextElement(sb.toString()));
            }
        }
        if (this.context.getPlugins().sqlMapSelectByExampleWithBLOBsElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
