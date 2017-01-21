/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lidroid.xutils.db.sqlite;

import android.R.bool;

import com.lidroid.xutils.db.table.ColumnUtils;
import com.lidroid.xutils.db.table.TableUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: wyouflf
 * Date: 13-8-9
 * Time: 下午10:19
 */
public class Selector {

    protected Class<?> entityType;
    protected String tableName;

    protected WhereBuilder whereBuilder;
    protected List<OrderBy> orderByList;
    protected int limit = 0;
    protected int offset = 0;

    private Selector(Class<?> entityType) {
        this.entityType = entityType;
        this.tableName = TableUtils.getTableName(entityType);
    }

    public static Selector from(Class<?> entityType) {
        return new Selector(entityType);
    }

    public Selector where(WhereBuilder whereBuilder) {
        this.whereBuilder = whereBuilder;
        return this;
    }

    public Selector where(String columnName, String op, Object value) {
        this.whereBuilder = WhereBuilder.b(changeFiledToColumn(columnName), op, value);
        return this;
    }

    public Selector and(String columnName, String op, Object value) {
        this.whereBuilder.and(changeFiledToColumn(columnName), op, value);
        return this;
    }

    public Selector and(WhereBuilder where) {
        this.whereBuilder.expr("AND (" + where.toString() + ")");
        return this;
    }

    public Selector or(String columnName, String op, Object value) {
        this.whereBuilder.or(changeFiledToColumn(columnName), op, value);
        return this;
    }

    public Selector or(WhereBuilder where) {
        this.whereBuilder.expr("OR (" + where.toString() + ")");
        return this;
    }

    public Selector expr(String expr) {
        if (this.whereBuilder == null) {
            this.whereBuilder = WhereBuilder.b();
        }
        this.whereBuilder.expr(expr);
        return this;
    }

    public Selector expr(String columnName, String op, Object value) {
        if (this.whereBuilder == null) {
            this.whereBuilder = WhereBuilder.b();
        }
        this.whereBuilder.expr(changeFiledToColumn(columnName), op, value);
        return this;
    }

    public DbModelSelector groupBy(String columnName) {
        return new DbModelSelector(this, changeFiledToColumn(columnName));
    }

    public DbModelSelector select(String... columnExpressions) {
        return new DbModelSelector(this, columnExpressions);
    }

    public Selector orderBy(String columnName) {
        if (orderByList == null) {
            orderByList = new ArrayList<OrderBy>(2);
        }
        orderByList.add(new OrderBy(changeFiledToColumn(columnName)));
        return this;
    }

    public Selector orderBy(String columnName, boolean desc) {
        if (orderByList == null) {
            orderByList = new ArrayList<OrderBy>(2);
        }
        orderByList.add(new OrderBy(changeFiledToColumn(columnName), desc));
        return this;
    }
    
    public Selector orderBy(String columnName, boolean desc,boolean code) {
    	if (orderByList == null) {
    		orderByList = new ArrayList<OrderBy>(2);
    	}
    	orderByList.add(new OrderBy(changeFiledToColumn(columnName), desc,code));
    	return this;
    }

    public Selector limit(int limit) {
        this.limit = limit;
        return this;
    }

    public Selector offset(int offset) {
        this.offset = offset;
        return this;
    }

    private String changeFiledToColumn(String columnName){
    	try {
    		columnName = ColumnUtils.getColumnNameByField(entityType.getDeclaredField(columnName));
		} catch (Exception e) {
			try {
				columnName = ColumnUtils.getColumnNameByField(entityType.getSuperclass().getDeclaredField(columnName));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
    	return columnName;
    } 
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("SELECT ");
        result.append("*");
        result.append(" FROM ").append(tableName);
        if (whereBuilder != null && whereBuilder.getWhereItemSize() > 0) {
            result.append(" WHERE ").append(whereBuilder.toString());
        }
        if (orderByList != null) {
            for (int i = 0; i < orderByList.size(); i++) {
            	if(i == 0){
            		result.append(" ORDER BY ");
            	}else {
            		result.append(" , ");
            	}
                result.append(orderByList.get(i).toString());
            }
        }
        if (limit > 0) {
            result.append(" LIMIT ").append(limit);
            result.append(" OFFSET ").append(offset);
        }
        return result.toString();
    }

    public Class<?> getEntityType() {
        return entityType;
    }

    protected class OrderBy {
        private String columnName;
        private boolean desc;
        private boolean code;

        public OrderBy(String columnName) {
            this.columnName = columnName;
        }

        public OrderBy(String columnName, boolean desc) {
            this.columnName = columnName;
            this.desc = desc;
        }
        
        public OrderBy(String columnName, boolean desc,boolean code) {
        	this.columnName = columnName;
        	this.desc = desc;
        	this.code = code;
        }

        @Override
        public String toString() {
        	if(!code)
        		return columnName + (desc ? " DESC" : " ASC");
        	return columnName + " COLLATE LOCALIZED " + (desc ? " DESC" : " ASC");
        }
    }
}
