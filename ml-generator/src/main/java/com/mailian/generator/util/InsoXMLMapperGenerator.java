package com.mailian.generator.util;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.internal.util.messages.Messages;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/27
 * @Description:
 */
public class InsoXMLMapperGenerator extends XMLMapperGenerator {

    public InsoXMLMapperGenerator() {
        super();
    }

    protected XmlElement getSqlMapElement() {
        FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
        this.progressCallback.startTask(Messages.getString("Progress.12", table.toString()));
        XmlElement answer = new XmlElement("mapper");
        String namespace = this.introspectedTable.getMyBatis3SqlMapNamespace();
        answer.addAttribute(new Attribute("namespace", namespace));
        this.context.getCommentGenerator().addRootComment(answer);
        this.addResultMapWithoutBLOBsElement(answer);
        this.addResultMapWithBLOBsElement(answer);
        this.addExampleWhereClauseElement(answer);
        this.addMyBatis3UpdateByExampleWhereClauseElement(answer);
        this.addBaseColumnListElement(answer);
        this.addBlobColumnListElement(answer);
        this.addSelectByExampleWithBLOBsElement(answer);
        this.addSelectByExampleWithoutBLOBsElement(answer);
        this.addSelectByPrimaryKeyElement(answer);
        this.addDeleteByPrimaryKeyElement(answer);
        this.addDeleteByExampleElement(answer);
        this.addInsertElement(answer);
        this.addInsertSelectiveElement(answer);
        this.addCountByExampleElement(answer);
        this.addUpdateByExampleSelectiveElement(answer);
        this.addUpdateByExampleWithBLOBsElement(answer);
        this.addUpdateByExampleWithoutBLOBsElement(answer);
        this.addUpdateByPrimaryKeySelectiveElement(answer);
        this.addUpdateByPrimaryKeyWithBLOBsElement(answer);
        this.addUpdateByPrimaryKeyWithoutBLOBsElement(answer);

        //add selectList
        this.addSelectList(answer);
        this.addDeleteByPrimaryKeyList(answer);
        this.addSelectBatchIds(answer);
        this.addInsertBatch(answer);
        return answer;
    }

    private void addInsertBatch(XmlElement parentElement) {
        if (introspectedTable.getRules()
                .generateInsert()) {
            AbstractXmlElementGenerator elementGenerator = new InsertBatchElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    private void addSelectBatchIds(XmlElement parentElement) {
        if (introspectedTable.getRules()
                .generateSelectByPrimaryKey()) {
            AbstractXmlElementGenerator elementGenerator = new SelectBatchIdsElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addSelectList(
            XmlElement parentElement) {
        if (introspectedTable.getRules()
                .generateSelectByPrimaryKey()) {
            AbstractXmlElementGenerator elementGenerator = new SelectListElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addDeleteByPrimaryKeyList(
            XmlElement parentElement) {
        if (introspectedTable.getRules()
                .generateDeleteByPrimaryKey()) {
            AbstractXmlElementGenerator elementGenerator = new DeleteByPrimaryKeyListElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

}
