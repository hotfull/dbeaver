/*
 * Copyright (C) 2013      Denis Forveille titou10.titou10@gmail.com
 * Copyright (C) 2010-2013 Serge Rieder serge@jkiss.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.jkiss.dbeaver.ext.db2.model;

import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.ext.db2.model.dict.DB2OwnerType;
import org.jkiss.dbeaver.ext.db2.model.dict.DB2YesNo;
import org.jkiss.dbeaver.model.DBPDataSource;
import org.jkiss.dbeaver.model.DBUtils;
import org.jkiss.dbeaver.model.impl.jdbc.JDBCUtils;
import org.jkiss.dbeaver.model.impl.jdbc.struct.JDBCTableConstraint;
import org.jkiss.dbeaver.model.meta.Property;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;
import org.jkiss.dbeaver.model.struct.DBSEntityAttributeRef;
import org.jkiss.dbeaver.model.struct.DBSEntityConstraintType;
import org.jkiss.utils.CommonUtils;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;

/**
 * DB2 Table Unique Key
 *
 * @author Denis Forveille
 */
public class DB2TableUniqueKey extends JDBCTableConstraint<DB2Table> {

    private String owner;
    private DB2OwnerType ownerType;
    private Boolean enforced;
    private Boolean trusted;
    private String checkExistingData; // TODO DF : Create Enum
    private Boolean enableQueryOpt;
    private String remarks;

    private List<DB2TableKeyColumn> columns;

    // -----------------
    // Constructor
    // -----------------

    public DB2TableUniqueKey(DBRProgressMonitor monitor, DB2Table table, ResultSet dbResult, DBSEntityConstraintType type) throws DBException
    {
        super(table, JDBCUtils.safeGetString(dbResult, "CONSTNAME"), null, type, true);

        this.owner = JDBCUtils.safeGetString(dbResult, "OWNER");
        this.ownerType = CommonUtils.valueOf(DB2OwnerType.class, JDBCUtils.safeGetString(dbResult, "OWNERTYPE"));
        this.enforced = JDBCUtils.safeGetBoolean(dbResult, "ENFORCED", DB2YesNo.Y.name());
        // DB2 v10 this.trusted = JDBCUtils.safeGetBoolean(dbResult, "TRUSTED", DB2YesNo.Y.name());
        this.checkExistingData = JDBCUtils.safeGetString(dbResult, "CHECKEXISTINGDATA");
        this.enableQueryOpt = JDBCUtils.safeGetBoolean(dbResult, "ENABLEQUERYOPT", DB2YesNo.Y.name());
        this.remarks = JDBCUtils.safeGetString(dbResult, "REMARKS");

    }

    @Override
    public String getFullQualifiedName()
    {
        return DBUtils.getFullQualifiedName(getDataSource(), getTable().getContainer(), getTable(), this);
    }

    @Override
    public DBPDataSource getDataSource()
    {
        return getTable().getDataSource();
    }

    // -----------------
    // Columns
    // -----------------

    @Override
    public Collection<? extends DBSEntityAttributeRef> getAttributeReferences(DBRProgressMonitor monitor) throws DBException
    {
        return columns;
    }

    public void setColumns(List<DB2TableKeyColumn> columns)
    {
        this.columns = columns;
    }

    // -----------------
    // Properties
    // -----------------
    @Override
    @Property(viewable = true, editable = false, order = 2)
    public DB2Table getTable()
    {
        return super.getTable();
    }

    @Override
    @Property(viewable = true, editable = false, order = 3)
    public DBSEntityConstraintType getConstraintType()
    {
        return super.getConstraintType();
    }

    @Override
    @Property(viewable = true, editable = false, order = 4)
    public String getDescription()
    {
        return remarks;
    }

    @Property(viewable = false, editable = false)
    public String getOwner()
    {
        return owner;
    }

    @Property(viewable = false, editable = false)
    public DB2OwnerType getOwnerType()
    {
        return ownerType;
    }

    @Property(viewable = false, editable = false)
    public Boolean getEnforced()
    {
        return enforced;
    }

    @Property(viewable = false, editable = false)
    public Boolean getTrusted()
    {
        return trusted;
    }

    @Property(viewable = false, editable = false)
    public String getCheckExistingData()
    {
        return checkExistingData;
    }

    @Property(viewable = false, editable = false)
    public Boolean getEnableQueryOpt()
    {
        return enableQueryOpt;
    }

}
