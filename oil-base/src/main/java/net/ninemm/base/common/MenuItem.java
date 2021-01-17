/*
 * Copyright (c) 2015-2018, Eric Huang 黄鑫 (ninemm@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ninemm.base.common;

import java.util.List;

/**
 * 菜单实体
 *
 * @author Eric
 *
 */
public class MenuItem {

    /** 菜单标题 */
    private String text;
    /** 菜单图标 */
    private String icon;
    /** 链接 */
    private String href;
    /** id */
    private Long mark;

    private List<MenuItem> subset;

    public MenuItem(String text, String icon, String href, Long mark) {
        this.text = text;
        this.icon = icon;
        this.href = href;
        this.mark = mark;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<MenuItem> getSubset() {
        return subset;
    }

    public void setSubset(List<MenuItem> subset) {
        this.subset = subset;
    }

    public Long getMark() {
        return mark;
    }

    public void setMark(Long mark) {
        this.mark = mark;
    }
}
