package SQLiteManager;

import SQLiteManager.*;
import org.junit.*;
import static org.junit.Assert.*;

public class QueryBuilderTest {

    private QueryBuilder firstQueryBuilder;
    private QueryBuilder secondQueryBuilder;
    private QueryBuilder thirdQueryBuilder;

    @Before
    public void setUp() {
        firstQueryBuilder = new QueryBuilder(QueryType.SELECT);
        secondQueryBuilder = new QueryBuilder(QueryType.INSERT);
        thirdQueryBuilder = new QueryBuilder(QueryType.DELETE);
        assertNotNull("First QueryBuilder could not be created",firstQueryBuilder);
        assertNotNull("Second QueryBuilder could not be created",secondQueryBuilder);
        assertNotNull("Third QueryBuilder could not be created",thirdQueryBuilder);
    }

    @Test
    public void testBuildSelectStatement() {
        String expectedSQLStatement =
                "SELECT a.aColumn, b.bColumn\n" +
                        "FROM aTable a\n" +
                        "WHERE a.aColumn=aValue AND b.bColumn=bValue\n" +
                        "GROUP BY a.aColumn\n" +
                        "HAVING a.aColumn > aValue\n" +
                        "ORDER BY a.aColumn ASC, b.bColumn DESC";
        firstQueryBuilder.addSelect(new String[] {"aColumn","bColumn"}, new String[] {"aTable","bTable"});
        firstQueryBuilder.addFrom(new String[] {"aTable"});
        firstQueryBuilder.addWhere(new String[] {"a.aColumn=aValue","b.bColumn=bValue"});
        firstQueryBuilder.addGroupBy(new String[] {"aColumn"}, new String[] {"aTable"});
        firstQueryBuilder.addHaving(new String[] {"a.aColumn > aValue"});
        firstQueryBuilder.addOrderBy(new String[] {"aColumn","bColumn"}, new String[] {"aTable","bTable"}, new String[] {"ASC","DESC"});
        assertEquals("Select SQL statement not correct, but should be",expectedSQLStatement,firstQueryBuilder.toString());
    }

    @Test
    public void testBuildInsertStatement() {
        String expectedSQLStatement =
                "INSERT INTO aTable (aColumn, bColumn)\n" +
                "VALUES (aValue,bValue)";
        secondQueryBuilder.addInsertTab("aTable");
        secondQueryBuilder.addInsertCols(new String[] {"aColumn","bColumn"});
        secondQueryBuilder.addInsertVals(new String[] {"aValue","bValue"});
        assertEquals("Insert SQL statement not correct, but should be",expectedSQLStatement,secondQueryBuilder.toString());
    }

    @Test
    public void testBuildDeleteStatement() {
        String expectedSQLStatement =
                "DELETE FROM aTable\n" +
                "WHERE aColumn=aValue AND bColumn=bValue";
        thirdQueryBuilder.addDeleteTab("aTable");
        thirdQueryBuilder.addDeleteWhere(new String[] {"aColumn=aValue","bColumn=bValue"});
        assertEquals("Delete SQL statement not correct, but should be",expectedSQLStatement,thirdQueryBuilder.toString());
    }

    @After
    public void tearDown() {
    }

}
