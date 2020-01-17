package com.thtf.common.core.constant;

/**
 * ---------------------------
 * 菜单常量
 * ---------------------------
 * 作者：  pyy
 * 时间：  2019/12/27 17:24
 * 版本：  v1.0
 * ---------------------------
 */
public class MenuConstant {

    /**
     * 菜单类型
     */
    public enum MenuType {

        CATALOG(0, "目录"),

        MENU(1,"菜单"),

        BUTTON(2, "按钮");

        private int value;
        private String name;

        MenuType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

}
