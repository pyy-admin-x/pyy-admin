package com.thtf.generate.api.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * ========================
 * 代码生成数据模型
 * Author：pyy
 * Date：2019-9-26 09:40:12
 * Version: v1.0
 * ========================
 */
@Data
public class GenerateModel {
	private String author;
	private String outPutFolderPath;
	private String basePackage = "com.thtf.output";
	private DataSource dataSource;
	private List<TableModel> tableModels = new ArrayList<>();
}
