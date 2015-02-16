package com.twopi.tutorial.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Class providing static method(s) for binding argumentes to prepared statements. Package scope only.
 * @author arshad01
 *
 */
class QueryBinder {

    enum ARGTYPE {
        Long,String,Double,Boolean,Timestamp,Nil;
    }
    
    static PreparedStatement bindArgs(PreparedStatement preparedStmt, Object... args) throws SQLException{
        
        int argIndex = 1;
        for (Object arg : args) {
            ARGTYPE argType = ARGTYPE.valueOf(arg != null ? arg.getClass().getSimpleName():"Nil");
            
            switch (argType) {
                case Long:      preparedStmt.setLong(argIndex++, (Long)arg); break;
                case String:    preparedStmt.setString(argIndex++, (String)arg); break;
                case Double:    preparedStmt.setDouble(argIndex++, (Double)arg); break;
                case Boolean:   preparedStmt.setBoolean(argIndex++, (Boolean)arg); break;
                case Timestamp: preparedStmt.setTimestamp(argIndex++, (Timestamp)arg); break;
                case Nil:       preparedStmt.setString(argIndex++, null);
            }
        }
        
        return preparedStmt;
    }
}
