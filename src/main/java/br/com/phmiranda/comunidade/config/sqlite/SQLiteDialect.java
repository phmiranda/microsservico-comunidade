package br.com.phmiranda.comunidade.config.sqlite;

import org.hibernate.JDBCException;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.IdentityColumnSupport;
import org.hibernate.dialect.identity.IdentityColumnSupportImpl;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.hibernate.engine.spi.RowSelection;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.LockAcquisitionException;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.exception.spi.SQLExceptionConversionDelegate;
import org.hibernate.exception.spi.TemplatedViolatedConstraintNameExtracter;
import org.hibernate.exception.spi.ViolatedConstraintNameExtracter;
import org.hibernate.internal.util.JdbcExceptionHelper;
import org.hibernate.type.StandardBasicTypes;

import java.sql.SQLException;
import java.sql.Types;

public class SQLiteDialect extends Dialect {

    private static final SQLiteIdentityColumnSupport IDENTITY_COLUMN_SUPPORT = new SQLiteIdentityColumnSupport();

    public SQLiteDialect() {
        registerColumnType(Types.BIT, "integer");
        registerColumnType(Types.TINYINT, "tinyint");
        registerColumnType(Types.SMALLINT, "smallint");
        registerColumnType(Types.INTEGER, "integer");
        registerColumnType(Types.BIGINT, "bigint");
        registerColumnType(Types.FLOAT, "float");
        registerColumnType(Types.REAL, "real");
        registerColumnType(Types.DOUBLE, "double");
        registerColumnType(Types.NUMERIC, "numeric");
        registerColumnType(Types.DECIMAL, "decimal");
        registerColumnType(Types.CHAR, "char");
        registerColumnType(Types.VARCHAR, "varchar");
        registerColumnType(Types.LONGVARCHAR, "longvarchar");
        registerColumnType(Types.DATE, "date");
        registerColumnType(Types.TIME, "time");
        registerColumnType(Types.TIMESTAMP, "datetime");
        registerColumnType(Types.BINARY, "blob");
        registerColumnType(Types.VARBINARY, "blob");
        registerColumnType(Types.LONGVARBINARY, "blob");
        registerColumnType(Types.BLOB, "blob");
        registerColumnType(Types.CLOB, "clob");
        registerColumnType(Types.BOOLEAN, "integer");

        registerFunction("concat", new VarArgsSQLFunction(StandardBasicTypes.STRING, "", "||", ""));
        registerFunction("mod", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "?1 % ?2"));
        registerFunction("quote", new StandardSQLFunction("quote", StandardBasicTypes.STRING));
        registerFunction("random", new StandardSQLFunction("random", StandardBasicTypes.INTEGER));
        registerFunction("round", new StandardSQLFunction("round"));
        registerFunction("substr", new StandardSQLFunction("substr", StandardBasicTypes.STRING));
        registerFunction("trim", new StandardSQLFunction("trim"));
    }

    @Override
    public IdentityColumnSupport getIdentityColumnSupport() {
        return IDENTITY_COLUMN_SUPPORT;
    }

    @Override
    public boolean hasAlterTable() {
        return false;
    }

    @Override
    public boolean dropConstraints() {
        return false;
    }

    @Override
    public String getAddColumnString() {
        return "add column";
    }

    @Override
    public String getForUpdateString() {
        return "";
    }

    @Override
    public boolean supportsOuterJoinForUpdate() {
        return false;
    }

    @Override
    public boolean supportsIfExistsBeforeTableName() {
        return true;
    }

    @Override
    public boolean supportsCascadeDelete() {
        return false;
    }

    @Override
    public boolean bindLimitParametersInReverseOrder() {
        return true;
    }

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    protected String getLimitString(String query, boolean hasOffset) {
        return query + (hasOffset ? " limit ? offset ?" : " limit ?");
    }

    @Override
    public boolean supportsLimitOffset() {
        return true;
    }

    @Override
    public boolean supportsVariableLimit() {
        return true;
    }

    @Override
    public SQLExceptionConversionDelegate buildSQLExceptionConversionDelegate() {
        return new SQLExceptionConversionDelegate() {
            @Override
            public JDBCException convert(SQLException sqlException, String message, String sql) {
                final int errorCode = JdbcExceptionHelper.extractErrorCode(sqlException);
                if (errorCode == 19) {
                    return new DataException(message, sqlException, sql);
                }

                final String sqlState = JdbcExceptionHelper.extractSqlState(sqlException);
                if (sqlState != null) {
                    if (sqlState.startsWith("23")) {
                        return new DataException(message, sqlException, sql);
                    }
                    if (sqlState.startsWith("42")) {
                        return new SQLGrammarException(message, sqlException, sql);
                    }
                    if (sqlState.startsWith("08")) {
                        return new JDBCConnectionException(message, sqlException, sql);
                    }
                }

                if (errorCode == 5 || errorCode == 6) {
                    return new LockAcquisitionException(message, sqlException, sql);
                }

                return null;
            }
        };
    }

    @Override
    public ViolatedConstraintNameExtracter getViolatedConstraintNameExtracter() {
        return EXTRACTER;
    }

    private static final ViolatedConstraintNameExtracter EXTRACTER = new TemplatedViolatedConstraintNameExtracter() {
        @Override
        protected String doExtractConstraintName(SQLException sqle) {
            return null;
        }
    };

    private static class SQLiteIdentityColumnSupport extends IdentityColumnSupportImpl {
        @Override
        public boolean supportsIdentityColumns() {
            return true;
        }

        @Override
        public String getIdentitySelectString(String table, String column, int type) {
            return "select last_insert_rowid()";
        }

        @Override
        public String getIdentityColumnString(int type) {
            return "integer";
        }
    }
}
