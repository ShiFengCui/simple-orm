
## Salad ORM - 简单小巧强大的orm框架
![GitHub last commit](https://img.shields.io/github/last-commit/ShiFengCui/salad-orm)
![GitHub repo size](https://img.shields.io/github/repo-size/ShiFengCui/salad-orm)
![GitHub issues](https://img.shields.io/github/issues/ShiFengCui/salad-orm)
![GitHub Repo stars](https://img.shields.io/github/stars/ShiFengCui/salad-orm?style=social)

![cover](https://user-images.githubusercontent.com/36906841/128585436-495d568b-0b68-4209-b290-58253f34ea4d.png)


**更适合的人群或者场景**

- 希望项目更加轻量级，快速实现对mysql增删改查操作的
- 希望学习java jdbc操作和具体封装实现的初学者，此项目更加清晰和容易理解 

#### 初始化

```java
@Slf4j
public class BaseDAO {


    public static DaoHelper daoHelper = null;

    static {
        try {
            daoHelper = DaoHelper.createIntrance(path() + "/druid.properties");
        } catch (Exception e) {
            log.error("初始化数据库异常", e);
        }
    }

    public static String path() {
        String path = BaseDAO.class.getResource("/").getPath();
        if (SystemUtils.IS_OS_WINDOWS) {
            return path + "/test";
        }
        return path + "/online";
    }
}
```

#### 创建DB服务

```java
@Table(name = "tbl_user")
public class User {

    @Id
    private Long id;
    
    // 如果java pojo实体字段和数据表字段不一致 这里需要增加一个注解标识
    @Column(name = "user_name")
    private String userName;

    private String phone;

    private String email;
    
    // get set 省略.....
}
```


```java
public interface UserService {

    /**
     * 根据userName获取用户信息
     *
     * @param userName
     * @return
     */
    User getUserByUserName(String userName);
 }
```

```java
@Slf4j
@Service
public class UserServiceImpl implements UserService {


    @Override
    public User getUserByUserName(String userName) {
        try {
            Assert.checkArgument(StringUtils.isBlank(userName), "用户名不能为空");
            DbQuery dbQuery = new DbQuery();
            dbQuery.column("user_name").equal(userName);
            User user = BaseDAO.daoHelper.getOneByQuery(User.class, dbQuery, "*", null);
            return user;
        } catch (Exception e) {
            log.error("根据用户名获取用户信息异常", e);
        }
        return null;
    }
 }
```

查询的重点就是构建查询条件 DbQuery

```java
 DbQuery dbQuery = new DbQuery();
            dbQuery.column("user_name").equal(userName);
            User user = BaseDAO.daoHelper.getOneByQuery(User.class, dbQuery, "*", null);
```

#### 快速的SQL编写查询

> 只是展示查询语法，跟商品User定义可能无关

- case1

```java
 public List<Role> getRolesByUserId(Long userId) {
        try {
            Assert.checkArgument(CheckParamUtils.isNullLong.test(userId), "用户id不能为空");
            String sql = " SELECT r.* FROM tbl_p_role r LEFT JOIN tbl_p_user_role ur on ur.role_id = r.id WHERE  r.`status` = 0 AND ur.user_id = ? ;";
            return BaseDAO.daoHelper.selectBySQL(Role.class, sql, userId);
        } catch (Exception e) {
            log.error("获取用户角色信息列表异常", e);
        }
        return null;
}
```

- case2

```java
 public List<Resource> getAllPermissionResource(Long userId) {
        try {
            Assert.checkArgument(CheckParamUtils.isNullLong.test(userId), "用户信息不能为空");
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT r.* FROM tbl_p_user_role ur  ");
            sql.append(" LEFT JOIN tbl_p_permission p ON p.role_id = ur.role_id ");
            sql.append(" LEFT JOIN tbl_p_resource r ON r.id = p.resource_id ");
            sql.append(" WHERE  p.`status` = 0 AND r.`status` = 0  AND ur.user_id = ?  ");
            return BaseDAO.daoHelper.selectBySQL(Resource.class, sql.toString(), userId);
        } catch (Exception e) {
            log.error("获取所有权限信息异常", e);
        }
        return null;
}
```



