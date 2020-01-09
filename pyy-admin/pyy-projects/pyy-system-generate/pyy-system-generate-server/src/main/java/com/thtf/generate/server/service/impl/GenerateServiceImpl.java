package com.thtf.generate.server.service.impl;

import com.thtf.generate.api.model.*;
import com.thtf.generate.server.service.DatabaseService;
import com.thtf.generate.server.service.GenerateService;
import com.thtf.generate.server.utils.DataTypeUtils;
import com.thtf.generate.server.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class GenerateServiceImpl implements GenerateService {
    public static final String TABLE = "table";

    public static final String TEMPLATE_MODEL = "/model.btl";
    public static final String TEMPLATE_COMMON_MODEL = "/commonModel.btl";
    public static final String TEMPLATE_VO = "/listVO.btl";
    public static final String TEMPLATE_QUERY_CONDITION_VO = "/queryConditionVO.btl";
    public static final String TEMPLATE_SAVE_UPDATE_VO = "/saveOrUpdateVO.btl";
    public static final String TEMPLATE_MAPPER = "/mapper.btl";
    public static final String TEMPLATE_SQLMAP = "/sqlMap.btl";
    public static final String TEMPLATE_SERVICE = "/service.btl";
    public static final String TEMPLATE_SERVICE_IMPL = "/serviceImpl.btl";
    public static final String TEMPLATE_CONTROLLER = "/controller.btl";
    public static final String TEMPLATE_CONTROLLER_API = "/controllerApi.btl";
    public static final String TEMPLATE_VIEW = "/view.btl";
    public static final String TEMPLATE_MODAL_VIEW = "/viewModal.btl";
    public static final String TEMPLATE_API_VIEW = "/viewApi.btl";


    public static final String PACKAGE_MODEL = "model";
    public static final String PACKAGE_VO = "vo";
    public static final String PACKAGE_DAO = "dao";
    public static final String PACKAGE_SQLMAP = "mappers";
    public static final String PACKAGE_SERVICE = "service";
    public static final String PACKAGE_SERVICE_IMPL = "service.impl";
    public static final String PACKAGE_CONTROLLER = "controller";
    public static final String PACKAGE_CONTROLLER_API = "controller.api";
    public static final String PACKAGE_VIEW = "view";

    public static final String SQL_MAP_SUFFIX = "Mapper.xml";
    public static final String MODEL_SUFFIX = ".java";
    public static final String LIST_VO_SUFFIX = "VO.java";
    public static final String COMMON_MODEL_SUFFIX = "CommonModel.java";
    public static final String QUERY_CONDITION_VO_SUFFIX = "QueryConditionVO.java";
    public static final String SAVE_UPDATE_VO_SUFFIX = "SaveOrUpdateVO.java";
    public static final String MAPPER_SUFFIX = "Mapper.java";
    public static final String SERVICE_SUFFIX = "Service.java";
    public static final String SERVICE_IMPL_SUFFIX = "ServiceImpl.java";
    public static final String CONTROLLER_SUFFIX = "Controller.java";
    public static final String CONTROLLER_API_SUFFIX = "ControllerApi.java";
    public static final String VIEW_SUFFIX = ".vue";
    public static final String JS_SUFFIX = ".js";


    @Autowired
    private DatabaseService databaseService;

    @Override
    public boolean testConnection(DataSource dataSource) {
        return databaseService.canConnect(dataSource);
    }

    @Override
    public List<Table> getTables(DataSource dataSource) {
        return databaseService.getTables(dataSource);
    }

    @Override
    public GenerateModel getGenerateModel(GenerateModel generateModel) {
        List<TableModel> tableModels = generateModel.getTableModels();
        for(TableModel tableModel:tableModels) {
            DataSource dataSource = generateModel.getDataSource();
            String tableName = tableModel.getName();
            // 设置表对应的实体名
            tableModel.setClassName(StringUtils.capitalize(lineToHump(tableName)));
            // 设置表对应的实例名
            tableModel.setObjectName(StringUtils.uncapitalize(tableModel.getClassName()));
            // 加载表字段
            tableModel.setColumns(getColumns(tableModel, dataSource, tableName));
        }
        return generateModel;
    }

    private List<ColumnModel> getColumns(TableModel tableModel, DataSource dataSource, String tableName) {
        List<ColumnModel> columnModels = new ArrayList<>();
        List<Column> columns = databaseService.getColumns(dataSource, tableName);
        List<PrimaryKey> primaryKeys = databaseService.getPrimaryKeys(dataSource, tableName);
        for(Column column:columns) {
            ColumnModel columnModel = new ColumnModel();
            BeanUtils.copyProperties(column, columnModel);
            // 设置字段对应的对象属性名
            String fieldName = lineToHump(column.getName());
            columnModel.setFieldName(fieldName);
            // 设置属性设置和获取方法
            String setter = "set" + StringUtils.capitalize(fieldName);
            columnModel.setSetter(setter);
            String getter = "get" + StringUtils.capitalize(fieldName);
            columnModel.setGetter(getter);
            // 设置JAVA数据类型
            String javaType = DataTypeUtils.getJavaType(columnModel.getDataType());
            columnModel.setJavaType(javaType);
            String jdbcType = DataTypeUtils.getJdbcType(columnModel.getDataType());
            columnModel.setJdbcType(jdbcType);
            // 设置属性是否为主键
            for(PrimaryKey primaryKey:primaryKeys) {
                if(column.getName().equalsIgnoreCase(primaryKey.getCloumn())) {
                    columnModel.setPrimaryKey(true);
                    tableModel.setPrimaryKey(columnModel);
                    break ;
                }
            }
            columnModels.add(columnModel);
        }
        return columnModels;
    }

    @Override
    public boolean generateModels(GenerateModel generateModel){
        String outPutFolderPath = generateModel.getOutPutFolderPath();
        if(outPutFolderPath == null) {
            outPutFolderPath = System.getProperty("user.dir") + "/src/main/java";
            generateModel.setOutPutFolderPath(outPutFolderPath);
        }
        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("templates");
        Configuration configuration = null;
        try {
            configuration = Configuration.defaultConfiguration();

            GroupTemplate groupTemplate = new GroupTemplate(resourceLoader, configuration);
            for(TableModel tableModel : generateModel.getTableModels()) {
                // 设置各类代码包名
                tableModel.setAuthor(generateModel.getAuthor());
                tableModel.setModelPackageName(getPakcageName(generateModel.getBasePackage(), PACKAGE_MODEL));
                tableModel.setVoPackageName(getPakcageName(generateModel.getBasePackage(), PACKAGE_VO));
                tableModel.setDaoPackageName(getPakcageName(generateModel.getBasePackage(), PACKAGE_DAO));
                tableModel.setSqlMapPackageName(getPakcageName(generateModel.getBasePackage(), PACKAGE_SQLMAP));
                tableModel.setServicePackageName(getPakcageName(generateModel.getBasePackage(), PACKAGE_SERVICE));
                tableModel.setServiceImplPackageName(getPakcageName(generateModel.getBasePackage(), PACKAGE_SERVICE_IMPL));
                tableModel.setControllerPackageName(getPakcageName(generateModel.getBasePackage(), PACKAGE_CONTROLLER));
                tableModel.setControllerPackageName(getPakcageName(generateModel.getBasePackage(), PACKAGE_CONTROLLER));
                tableModel.setControllerApiPackageName(getPakcageName(generateModel.getBasePackage(), PACKAGE_CONTROLLER_API));
                tableModel.setViewPackageName(getPakcageName(generateModel.getBasePackage(), PACKAGE_VIEW));
                // generate model
                generateModel(groupTemplate, tableModel, TEMPLATE_MODEL, generateModel.getOutPutFolderPath());
                // generate commonModel
                generateModel(groupTemplate, tableModel, TEMPLATE_COMMON_MODEL, generateModel.getOutPutFolderPath());
                // generate queryConditionVO
                generateModel(groupTemplate, tableModel, TEMPLATE_QUERY_CONDITION_VO, generateModel.getOutPutFolderPath());
                // generate listVO
                generateModel(groupTemplate, tableModel, TEMPLATE_VO, generateModel.getOutPutFolderPath());
                // generate saveOrUpdateVO
                generateModel(groupTemplate, tableModel, TEMPLATE_SAVE_UPDATE_VO, generateModel.getOutPutFolderPath());
                // generate mapper
                generateModel(groupTemplate, tableModel, TEMPLATE_MAPPER, generateModel.getOutPutFolderPath());
                // generate sqlmap.xml
                generateModel(groupTemplate, tableModel, TEMPLATE_SQLMAP, generateModel.getOutPutFolderPath());
                // generate service
                generateModel(groupTemplate, tableModel, TEMPLATE_SERVICE, generateModel.getOutPutFolderPath());
                // generate serviceImpl
                generateModel(groupTemplate, tableModel, TEMPLATE_SERVICE_IMPL, generateModel.getOutPutFolderPath());
                // generate controller
                generateModel(groupTemplate, tableModel, TEMPLATE_CONTROLLER, generateModel.getOutPutFolderPath());
                // generate controllerApi
                generateModel(groupTemplate, tableModel, TEMPLATE_CONTROLLER_API, generateModel.getOutPutFolderPath());

                // vue页面相关
                // generate view
                generateModel(groupTemplate, tableModel, TEMPLATE_VIEW, generateModel.getOutPutFolderPath());
                // generate viewModal
                generateModel(groupTemplate, tableModel, TEMPLATE_MODAL_VIEW, generateModel.getOutPutFolderPath());
                // generate viewApi
                generateModel(groupTemplate, tableModel, TEMPLATE_API_VIEW, generateModel.getOutPutFolderPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private String getPakcageName(String basePackage, String subPackage) {
        // TODO Auto-generated method stub
        return basePackage + "." + subPackage;
    }

    /**
     * 生成代码
     * @param groupTemplate
     * @param tableModel
     * @param templatePath
     * @param outPutFolderPath
     * @throws
     * @throws Exception
     */
    private void generateModel(GroupTemplate groupTemplate, TableModel tableModel, String templatePath, String outPutFolderPath) throws Exception {
        Template template = groupTemplate.getTemplate(templatePath);
        template.binding(TABLE, tableModel);
        FileOutputStream os = new FileOutputStream(getOutputFile(tableModel, outPutFolderPath, templatePath));
        template.renderTo(os);
        os.close();
    }

    /**
     * 获取要生成的文件
     * @param tableModel
     * @param outPutFolderPath
     * @param templatePath
     * @return
     */
    private String getOutputFile(TableModel tableModel, String outPutFolderPath, String templatePath) {
        String packageName = tableModel.getModelPackageName();
        String suffix = MODEL_SUFFIX;
        if(TEMPLATE_SAVE_UPDATE_VO.equals(templatePath)) {
            packageName = tableModel.getVoPackageName();
            suffix = SAVE_UPDATE_VO_SUFFIX;
        } else if(TEMPLATE_COMMON_MODEL.equals(templatePath)) {
            packageName = tableModel.getModelPackageName();
            suffix = COMMON_MODEL_SUFFIX;
        } else if(TEMPLATE_QUERY_CONDITION_VO.equals(templatePath)) {
            packageName = tableModel.getVoPackageName();
            suffix = QUERY_CONDITION_VO_SUFFIX;
        } else if(TEMPLATE_VO.equals(templatePath)) {
            packageName = tableModel.getVoPackageName();
            suffix = LIST_VO_SUFFIX;
        } else if(TEMPLATE_MAPPER.equals(templatePath)) {
            packageName = tableModel.getDaoPackageName();
            suffix = MAPPER_SUFFIX;
        } else if(TEMPLATE_SQLMAP.equals(templatePath)) {
            packageName = tableModel.getSqlMapPackageName();
            suffix = SQL_MAP_SUFFIX;
        } else if(TEMPLATE_SERVICE.equals(templatePath)) {
            packageName = tableModel.getServicePackageName();
            suffix = SERVICE_SUFFIX;
        } else if(TEMPLATE_SERVICE_IMPL.equals(templatePath)) {
            packageName = tableModel.getServiceImplPackageName();
            suffix = SERVICE_IMPL_SUFFIX;
        } else if(TEMPLATE_CONTROLLER.equals(templatePath)) {
            packageName = tableModel.getControllerPackageName();
            suffix = CONTROLLER_SUFFIX;
        } else if(TEMPLATE_CONTROLLER_API.equals(templatePath)) {
            packageName = tableModel.getControllerApiPackageName();
            suffix = CONTROLLER_API_SUFFIX;
        } else if(TEMPLATE_VIEW.equals(templatePath)) {
            packageName = tableModel.getViewPackageName();
            suffix = VIEW_SUFFIX;
        } else if(TEMPLATE_MODAL_VIEW.equals(templatePath)) {
            packageName = tableModel.getViewPackageName();
            suffix = VIEW_SUFFIX;
        } else if(TEMPLATE_API_VIEW.equals(templatePath)) {
            packageName = tableModel.getViewPackageName();
            suffix = JS_SUFFIX;
        }
        outPutFolderPath = outPutFolderPath + "/" + packageName.replaceAll("\\.", "/");
        File outPutFolder = new File(outPutFolderPath);
        if(!outPutFolder.exists()) {
            outPutFolder.mkdirs();
        }

        String filePath = outPutFolderPath + File.separator + tableModel.getClassName() + suffix;
        if(TEMPLATE_COMMON_MODEL.equals(templatePath)) {
            filePath = outPutFolderPath + File.separator + suffix;
        }

        if(TEMPLATE_VIEW.equals(templatePath)) {
            filePath = outPutFolderPath + File.separator + "index" + VIEW_SUFFIX;
        }

        if(TEMPLATE_MODAL_VIEW.equals(templatePath)) {
            filePath = outPutFolderPath + File.separator + tableModel.getObjectName() + "Modal" + VIEW_SUFFIX;
        }

        if(TEMPLATE_API_VIEW.equals(templatePath)) {
            filePath = outPutFolderPath + File.separator + "index" + JS_SUFFIX;
        }

        File file = new File(filePath);
        if(file.exists()) {
            file.delete();
        }
        return filePath;
    }

    /**
     * 下划线转驼峰式
     * @param str
     * @return
     */
    public String lineToHump(String str) {
        return StringUtils.lineToHump(str);
    }
}
