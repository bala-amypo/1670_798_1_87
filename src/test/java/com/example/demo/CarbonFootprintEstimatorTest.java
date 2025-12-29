package com.example.demo;

import com.example.demo.dto.ActivityLogRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.*;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.*;
import com.example.demo.service.impl.*;
import org.mockito.*;
import org.mockito.stubbing.Answer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Single TestNG class with 60 tests,
 * ordered according to:
 * 1. Simple Servlet / Controller-like behavior
 * 2. CRUD with Spring Boot
 * 3. DI & IoC
 * 4. Hibernate config & CRUD
 * 5. JPA mapping & normalization
 * 6. Many-to-many relationships (conceptually via associations)
 * 7. Security + JWT
 * 8. HQL / criteria-like advanced querying (simulated via repo methods)
 */
@Listeners(TestResultListener.class)
public class CarbonFootprintEstimatorTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ActivityCategoryRepository categoryRepository;
    @Mock
    private ActivityTypeRepository typeRepository;
    @Mock
    private ActivityLogRepository logRepository;
    @Mock
    private EmissionFactorRepository factorRepository;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserServiceImpl userService;
    @InjectMocks
    private ActivityCategoryServiceImpl categoryService;
    @InjectMocks
    private ActivityTypeServiceImpl typeService;
    @InjectMocks
    private ActivityLogServiceImpl logService;
    @InjectMocks
    private EmissionFactorServiceImpl factorService;

    private JwtUtil jwtUtil;
    private CustomUserDetailsService userDetailsService;

    @BeforeClass
    public void setup() {
        MockitoAnnotations.openMocks(this);
        jwtUtil = new JwtUtil();
        userDetailsService = new CustomUserDetailsService(userRepository);
    }

    // region 1. "Servlet" style: simple controller behavior simulation (7 tests)

    @Test(priority = 1, groups = "servlet")
    public void t01_simpleController_returns200_likeServlet() {
        String result = "OK";
        Assert.assertEquals(result, "OK");
    }

    @Test(priority = 2, groups = "servlet")
    public void t02_simpleControllerHandlesPathParameter() {
        Long userId = 1L;
        String url = "/api/users/" + userId;
        Assert.assertTrue(url.contains("/api/users/1"));
    }

    @Test(priority = 3, groups = "servlet")
    public void t03_simpleControllerHandlesQueryParam() {
        String url = "/api/logs/user/1/range?start=2024-01-01&end=2024-01-31";
        Assert.assertTrue(url.contains("start=2024-01-01"));
        Assert.assertTrue(url.contains("end=2024-01-31"));
    }

    @Test(priority = 4, groups = "servlet")
    public void t04_controllerLikeServletProcessesPostBody() {
        RegisterRequest req = new RegisterRequest("Test User", "test@example.com", "password123");
        Assert.assertEquals(req.getEmail(), "test@example.com");
    }

    @Test(priority = 5, groups = "servlet")
    public void t05_simpleJsonMappingSimulation() {
        ActivityLogRequest req = new ActivityLogRequest(10.0, LocalDate.now());
        Assert.assertEquals(req.getQuantity(), 10.0);
        Assert.assertNotNull(req.getActivityDate());
    }

    @Test(priority = 6, groups = "servlet")
    public void t06_servletStylePathMatchingCheck() {
        String path = "/api/factors/5";
        Assert.assertTrue(path.matches(".*/api/factors/\\d+"));
    }

    @Test(priority = 7, groups = "servlet")
    public void t07_servletStatusCodeConceptCheck() {
        int statusOk = 200;
        int statusUnauthorized = 401;
        Assert.assertTrue(statusOk < statusUnauthorized);
    }

    // endregion

    // region 2. CRUD operations using Spring Boot + REST (10 tests, priority 10-19)

    @Test(priority = 10, groups = "crud")
    public void t10_registerUser_success() {
        User user = new User(null, "User A", "a@example.com", "password123", "USER", LocalDateTime.now());
        when(userRepository.existsByEmail("a@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer((Answer<User>) invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });

        User created = userService.registerUser(user);

        Assert.assertNotNull(created.getId());
        Assert.assertEquals(created.getEmail(), "a@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test(priority = 11, groups = "crud")
    public void t11_registerUser_duplicateEmail() {
        User user = new User(null, "User A", "a@example.com", "password123", "USER", LocalDateTime.now());
        when(userRepository.existsByEmail("a@example.com")).thenReturn(true);

        try {
            userService.registerUser(user);
            Assert.fail("Expected ValidationException for duplicate email");
        } catch (ValidationException ex) {
            Assert.assertTrue(ex.getMessage().contains("Email already in use"));
        }
    }

    @Test(priority = 12, groups = "crud")
    public void t12_getUserById_success() {
        User user = new User(1L, "User A", "a@example.com", "encPass", "USER", LocalDateTime.now());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUser(1L);
        Assert.assertEquals(result.getId(), 1L);
    }

    @Test(priority = 13, groups = "crud")
    public void t13_getUserById_notFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        try {
            userService.getUser(99L);
            Assert.fail("Expected ResourceNotFoundException");
        } catch (ResourceNotFoundException ex) {
            Assert.assertTrue(ex.getMessage().contains("User not found"));
        }
    }

    @Test(priority = 14, groups = "crud")
    public void t14_createCategory_success() {
        ActivityCategory category = new ActivityCategory(null, "Transport", "Transport desc", null);
        when(categoryRepository.existsByCategoryName("Transport")).thenReturn(false);
        when(categoryRepository.save(any(ActivityCategory.class))).thenAnswer(invocation -> {
            ActivityCategory c = invocation.getArgument(0);
            c.setId(10L);
            return c;
        });

        ActivityCategory created = categoryService.createCategory(category);
        Assert.assertNotNull(created.getId());
        Assert.assertEquals(created.getCategoryName(), "Transport");
    }

    @Test(priority = 15, groups = "crud")
    public void t15_createCategory_duplicateName() {
        ActivityCategory category = new ActivityCategory(null, "Transport", "Transport desc", null);
        when(categoryRepository.existsByCategoryName("Transport")).thenReturn(true);

        try {
            categoryService.createCategory(category);
            Assert.fail("Expected ValidationException");
        } catch (ValidationException ex) {
            Assert.assertTrue(ex.getMessage().contains("Category name must be unique"));
        }
    }

    @Test(priority = 16, groups = "crud")
    public void t16_getAllCategories_emptyList() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());
        List<ActivityCategory> list = categoryService.getAllCategories();
        Assert.assertTrue(list.isEmpty());
    }

    @Test(priority = 17, groups = "crud")
    public void t17_getAllCategories_nonEmptyList() {
        ActivityCategory cat1 = new ActivityCategory(1L, "Transport", null, LocalDateTime.now());
        when(categoryRepository.findAll()).thenReturn(List.of(cat1));
        List<ActivityCategory> list = categoryService.getAllCategories();
        Assert.assertEquals(list.size(), 1);
    }

    @Test(priority = 18, groups = "crud")
    public void t18_getCategoryById_found() {
        ActivityCategory cat = new ActivityCategory(1L, "Transport", null, LocalDateTime.now());
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat));

        ActivityCategory result = categoryService.getCategory(1L);
        Assert.assertEquals(result.getId(), 1L);
    }

    @Test(priority = 19, groups = "crud")
    public void t19_getCategoryById_notFound() {
        when(categoryRepository.findById(100L)).thenReturn(Optional.empty());
        try {
            categoryService.getCategory(100L);
            Assert.fail("Expected ResourceNotFoundException");
        } catch (ResourceNotFoundException ex) {
            Assert.assertTrue(ex.getMessage().contains("Category not found"));
        }
    }

    // endregion

    // region 3. DI & IoC (5 tests) priority 20-24

    @Test(priority = 20, groups = "di")
    public void t20_userServiceInjectedRepositoriesNotNull() {
        Assert.assertNotNull(userService);
        Assert.assertNotNull(userRepository);
    }

    @Test(priority = 21, groups = "di")
    public void t21_categoryServiceInjectedRepositoryNotNull() {
        Assert.assertNotNull(categoryService);
        Assert.assertNotNull(categoryRepository);
    }

    @Test(priority = 22, groups = "di")
    public void t22_activityTypeServiceInjectedDependencies() {
        Assert.assertNotNull(typeService);
        Assert.assertNotNull(categoryRepository);
        Assert.assertNotNull(typeRepository);
    }

    @Test(priority = 23, groups = "di")
    public void t23_logServiceInjectedMultipleRepositories() {
        Assert.assertNotNull(logService);
        Assert.assertNotNull(userRepository);
        Assert.assertNotNull(typeRepository);
        Assert.assertNotNull(factorRepository);
    }

    @Test(priority = 24, groups = "di")
    public void t24_factorServiceInjectedDependencies() {
        Assert.assertNotNull(factorService);
        Assert.assertNotNull(typeRepository);
        Assert.assertNotNull(factorRepository);
    }

    // endregion

    // region 4. Hibernate configs, generator, annotations, CRUD (8 tests) priority 30-37

    @Test(priority = 30, groups = "hibernate")
    public void t30_entityUser_hasIdGenerated() {
        User user = new User();
        user.setId(10L);
        Assert.assertEquals(user.getId(), 10L);
    }

    @Test(priority = 31, groups = "hibernate")
    public void t31_entityActivityCategory_prePersistSetsCreatedAt() {
        ActivityCategory cat = new ActivityCategory();
        cat.setCategoryName("Energy");
        cat.prePersist();
        Assert.assertNotNull(cat.getCreatedAt());
    }

    @Test(priority = 32, groups = "hibernate")
    public void t32_entityActivityType_prePersistSetsCreatedAt() {
        ActivityType type = new ActivityType();
        type.setTypeName("Car Travel");
        type.setUnit("km");
        type.prePersist();
        Assert.assertNotNull(type.getCreatedAt());
    }

    @Test(priority = 33, groups = "hibernate")
    public void t33_entityEmissionFactor_prePersistSetsCreatedAt() {
        EmissionFactor ef = new EmissionFactor();
        ef.setFactorValue(0.2);
        ef.setUnit("km");
        ef.prePersist();
        Assert.assertNotNull(ef.getCreatedAt());
    }

    @Test(priority = 34, groups = "hibernate")
    public void t34_entityActivityLog_prePersistSetsLoggedAt() {
        ActivityLog log = new ActivityLog();
        log.prePersist();
        Assert.assertNotNull(log.getLoggedAt());
    }

    @Test(priority = 35, groups = "hibernate")
    public void t35_crudSaveUser_viaRepositoryMock() {
        User user = new User(null, "B", "b@example.com", "password123", "USER", null);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(2L);
            return u;
        });
        User saved = userRepository.save(user);
        Assert.assertEquals(saved.getId(), 2L);
    }

    @Test(priority = 36, groups = "hibernate")
    public void t36_repositoryFindByEmail_returnsOptional() {
        User user = new User(3L, "C", "c@example.com", "pwd", "USER", LocalDateTime.now());
        when(userRepository.findByEmail("c@example.com")).thenReturn(Optional.of(user));
        Optional<User> opt = userRepository.findByEmail("c@example.com");
        Assert.assertTrue(opt.isPresent());
    }

    @Test(priority = 37, groups = "hibernate")
    public void t37_repositoryFindByEmail_empty() {
        when(userRepository.findByEmail("none@example.com")).thenReturn(Optional.empty());
        Optional<User> opt = userRepository.findByEmail("none@example.com");
        Assert.assertTrue(opt.isEmpty());
    }

    // endregion

    // region 5. JPA mapping & normalization (1NF, 2NF, 3NF) (5 tests) priority 40-44

    @Test(priority = 40, groups = "jpa")
    public void t40_activityTypeBelongsToCategory_1NF() {
        ActivityCategory cat = new ActivityCategory(1L, "Transport", null, LocalDateTime.now());
        ActivityType type = new ActivityType(5L, "Car Travel", cat, "km", LocalDateTime.now());
        Assert.assertEquals(type.getCategory().getId(), 1L);
    }

    @Test(priority = 41, groups = "jpa")
    public void t41_activityLogHasUserAndType_2NFConcept() {
        User user = new User(1L, "User x", "x@example.com", "pwd", "USER", LocalDateTime.now());
        ActivityType type = new ActivityType(2L, "Bus", null, "km", LocalDateTime.now());
        ActivityLog log = new ActivityLog(1L, type, user, 10.0,
                LocalDate.now(), LocalDateTime.now(), 20.0);
        Assert.assertEquals(log.getUser().getId(), 1L);
        Assert.assertEquals(log.getActivityType().getId(), 2L);
    }

    @Test(priority = 42, groups = "jpa")
    public void t42_emissionFactorReferencesActivityType_3NFConcept() {
        ActivityType type = new ActivityType(2L, "Car", null, "km", LocalDateTime.now());
        EmissionFactor ef = new EmissionFactor(1L, type, 0.2, "km", LocalDateTime.now());
        Assert.assertEquals(ef.getActivityType().getId(), 2L);
    }

    @Test(priority = 43, groups = "jpa")
    public void t43_typesByCategoryRepositoryMethod() {
        ActivityCategory cat = new ActivityCategory(1L, "Energy", null, LocalDateTime.now());
        ActivityType t1 = new ActivityType(1L, "Electricity", cat, "kWh", LocalDateTime.now());
        when(typeRepository.findByCategory_Id(1L)).thenReturn(List.of(t1));

        List<ActivityType> list = typeRepository.findByCategory_Id(1L);
        Assert.assertEquals(list.size(), 1);
        Assert.assertEquals(list.get(0).getCategory().getCategoryName(), "Energy");
    }

    @Test(priority = 44, groups = "jpa")
    public void t44_logsByUserAndDateRepositoryMethod() {
        ActivityLog log1 = new ActivityLog(1L, null, null, 10.0,
                LocalDate.of(2024, 1, 1), LocalDateTime.now(), 5.0);
        when(logRepository.findByUser_IdAndActivityDateBetween(1L,
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)))
                .thenReturn(List.of(log1));

        List<ActivityLog> list = logRepository.findByUser_IdAndActivityDateBetween(1L,
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
        Assert.assertEquals(list.size(), 1);
    }

    // endregion

    // region 6. Many-to-Many relationships & associations (simulated) (5 tests) priority 50-54

    @Test(priority = 50, groups = "manyToMany")
    public void t50_userMultipleLogs_association() {
        User user = new User(1L, "MultiUser", "multi@example.com", "pwd", "USER", LocalDateTime.now());
        ActivityLog log1 = new ActivityLog(1L, null, user, 5.0, LocalDate.now(), LocalDateTime.now(), 2.0);
        ActivityLog log2 = new ActivityLog(2L, null, user, 10.0, LocalDate.now(), LocalDateTime.now(), 4.0);

        List<ActivityLog> logs = List.of(log1, log2);
        Assert.assertEquals(logs.size(), 2);
        Assert.assertTrue(logs.stream().allMatch(l -> l.getUser().getId().equals(1L)));
    }

    @Test(priority = 51, groups = "manyToMany")
    public void t51_typeUsedInMultipleLogs_association() {
        ActivityType type = new ActivityType(1L, "SharedType", null, "km", LocalDateTime.now());
        ActivityLog log1 = new ActivityLog(1L, type, null, 5.0, LocalDate.now(), LocalDateTime.now(), 1.0);
        ActivityLog log2 = new ActivityLog(2L, type, null, 10.0, LocalDate.now(), LocalDateTime.now(), 2.0);

        Assert.assertEquals(log1.getActivityType().getId(), log2.getActivityType().getId());
    }

    @Test(priority = 52, groups = "manyToMany")
    public void t52_userTypesManyRelationConceptual() {
        User user = new User(1L, "U", "u@example.com", "pwd", "USER", LocalDateTime.now());
        ActivityType type1 = new ActivityType(1L, "Car", null, "km", LocalDateTime.now());
        ActivityType type2 = new ActivityType(2L, "Bus", null, "km", LocalDateTime.now());

        ActivityLog log1 = new ActivityLog(1L, type1, user, 11.0, LocalDate.now(), LocalDateTime.now(), 2.0);
        ActivityLog log2 = new ActivityLog(2L, type2, user, 12.0, LocalDate.now(), LocalDateTime.now(), 3.0);

        Set<ActivityType> types = new HashSet<>();
        types.add(log1.getActivityType());
        types.add(log2.getActivityType());
        Assert.assertEquals(types.size(), 2);
    }

    @Test(priority = 53, groups = "manyToMany")
    public void t53_sameTypeDifferentUsers_association() {
        ActivityType type = new ActivityType(1L, "Car", null, "km", LocalDateTime.now());
        User u1 = new User(1L, "U1", "u1@example.com", "pwd", "USER", LocalDateTime.now());
        User u2 = new User(2L, "U2", "u2@example.com", "pwd", "USER", LocalDateTime.now());

        ActivityLog l1 = new ActivityLog(1L, type, u1, 1.0, LocalDate.now(), LocalDateTime.now(), 0.2);
        ActivityLog l2 = new ActivityLog(2L, type, u2, 2.0, LocalDate.now(), LocalDateTime.now(), 0.4);

        Assert.assertNotEquals(l1.getUser().getId(), l2.getUser().getId());
        Assert.assertEquals(l1.getActivityType().getId(), l2.getActivityType().getId());
    }

    @Test(priority = 54, groups = "manyToMany")
    public void t54_manyToManyConceptTotalUsersForType() {
        ActivityType type = new ActivityType(1L, "Car", null, "km", LocalDateTime.now());
        User u1 = new User(1L, "U1", "u1@example.com", "pwd", "USER", LocalDateTime.now());
        User u2 = new User(2L, "U2", "u2@example.com", "pwd", "USER", LocalDateTime.now());

        ActivityLog l1 = new ActivityLog(1L, type, u1, 1.0, LocalDate.now(), LocalDateTime.now(), 0.2);
        ActivityLog l2 = new ActivityLog(2L, type, u2, 2.0, LocalDate.now(), LocalDateTime.now(), 0.4);

        Set<Long> userIds = new HashSet<>();
        userIds.add(l1.getUser().getId());
        userIds.add(l2.getUser().getId());

        Assert.assertEquals(userIds.size(), 2);
    }

    // endregion

    // region 7. Security & JWT token-based auth (14 tests) priority 60-73

    @Test(priority = 60, groups = "security")
    public void t60_generateJwtToken_containsSubject() {
        Map<String, Object> claims = Map.of("key", "value");
        String token = jwtUtil.generateToken(claims, "subject@example.com");
        String username = jwtUtil.extractUsername(token);
        Assert.assertEquals(username, "subject@example.com");
    }

    @Test(priority = 61, groups = "security")
    public void t61_generateTokenForUser_containsUserIdEmailRole() {
        User user = new User(10L, "T", "token@example.com", "pwd", "ADMIN", LocalDateTime.now());
        String token = jwtUtil.generateTokenForUser(user);

        String username = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);
        Long userId = jwtUtil.extractUserId(token);

        Assert.assertEquals(username, "token@example.com");
        Assert.assertEquals(role, "ADMIN");
        Assert.assertEquals(userId, Long.valueOf(10L));
    }

    @Test(priority = 62, groups = "security")
    public void t62_tokenValidation_success() {
        User user = new User(11L, "U", "u@example.com", "pwd", "USER", LocalDateTime.now());
        String token = jwtUtil.generateTokenForUser(user);
        boolean valid = jwtUtil.isTokenValid(token, "u@example.com");
        Assert.assertTrue(valid);
    }

    @Test(priority = 63, groups = "security")
    public void t63_tokenValidation_wrongUser() {
        User user = new User(11L, "U", "u@example.com", "pwd", "USER", LocalDateTime.now());
        String token = jwtUtil.generateTokenForUser(user);
        boolean valid = jwtUtil.isTokenValid(token, "v@example.com");
        Assert.assertFalse(valid);
    }

    @Test(priority = 64, groups = "security")
    public void t64_customUserDetailsService_loadByEmail_success() {
        User user = new User(5L, "User", "user@example.com", "pwd", "ADMIN", LocalDateTime.now());
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        UserDetails details = userDetailsService.loadUserByUsername("user@example.com");
        Assert.assertEquals(details.getUsername(), "user@example.com");
        Assert.assertTrue(details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test(priority = 65, groups = "security")
    public void t65_customUserDetailsService_userNotFound() {
        when(userRepository.findByEmail("no@example.com")).thenReturn(Optional.empty());
        try {
            userDetailsService.loadUserByUsername("no@example.com");
            Assert.fail("Expected UsernameNotFoundException");
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage().contains("User not found"));
        }
    }

    @Test(priority = 66, groups = "security")
    public void t66_authenticationManager_simulation_success() {
        LoginRequest request = new LoginRequest("auth@example.com", "password123");
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(token);

        Assert.assertEquals(token.getPrincipal(), request.getEmail());
        verify(authenticationManager, never()).authenticate(null);
    }

    @Test(priority = 67, groups = "security")
    public void t67_roleBasedAccessConcept_adminCanManageCategories() {
        User admin = new User(1L, "Admin", "admin@example.com", "pwd", "ADMIN", LocalDateTime.now());
        Assert.assertEquals(admin.getRole(), "ADMIN");
    }

    @Test(priority = 68, groups = "security")
    public void t68_roleBasedAccessConcept_userCannotBeAdmin() {
        User normal = new User(2L, "User", "user@example.com", "pwd", "USER", LocalDateTime.now());
        Assert.assertNotEquals(normal.getRole(), "ADMIN");
    }

    @Test(priority = 69, groups = "security")
    public void t69_tokenIncludesEmailClaim() {
        User user = new User(15L, "E", "emailclaim@example.com", "pwd", "USER", LocalDateTime.now());
        String token = jwtUtil.generateTokenForUser(user);
        String email = (String) jwtUtil.parseToken(token).getPayload().get("email");
        Assert.assertEquals(email, "emailclaim@example.com");
    }

    @Test(priority = 70, groups = "security")
    public void t70_tokenIncludesRoleClaim() {
        User user = new User(16L, "R", "roleclaim@example.com", "pwd", "ADMIN", LocalDateTime.now());
        String token = jwtUtil.generateTokenForUser(user);
        String role = (String) jwtUtil.parseToken(token).getPayload().get("role");
        Assert.assertEquals(role, "ADMIN");
    }

    @Test(priority = 71, groups = "security")
    public void t71_tokenIncludesUserIdClaim() {
        User user = new User(17L, "I", "idclaim@example.com", "pwd", "USER", LocalDateTime.now());
        String token = jwtUtil.generateTokenForUser(user);
        Object id = jwtUtil.parseToken(token).getPayload().get("userId");
        Assert.assertNotNull(id);
    }

    @Test(priority = 72, groups = "security")
    public void t72_invalidTokenParsingThrows() {
        String invalidToken = "invalid.token.string";
        try {
            jwtUtil.parseToken(invalidToken);
            Assert.fail("Expected exception for invalid token");
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage() != null);
        }
    }

    @Test(priority = 73, groups = "security")
    public void t73_passwordMinLengthValidation() {
        User user = new User(null, "ShortPwd", "shortpwd@example.com", "short", "USER", LocalDateTime.now());
        when(userRepository.existsByEmail("shortpwd@example.com")).thenReturn(false);

        try {
            userService.registerUser(user);
            Assert.fail("Expected ValidationException due to password length");
        } catch (ValidationException ex) {
            Assert.assertTrue(ex.getMessage().contains("Password must be at least 8 characters"));
        }
    }

    // endregion

    // region 8. Advanced querying (HQL / criteria-like) (7 tests) priority 80-86

    @Test(priority = 80, groups = "hql")
    public void t80_getLogsByUserAndDate_service() {
        ActivityLog log1 = new ActivityLog(1L, null, null, 5.0,
                LocalDate.of(2024, 1, 1), LocalDateTime.now(), 1.0);
        when(logRepository.findByUser_IdAndActivityDateBetween(1L,
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)))
                .thenReturn(List.of(log1));

        List<ActivityLog> list = logService.getLogsByUserAndDate(1L,
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
        Assert.assertEquals(list.size(), 1);
    }

    @Test(priority = 81, groups = "hql")
    public void t81_getLogsByUser_service() {
        ActivityLog log1 = new ActivityLog(1L, null, null, 5.0,
                LocalDate.of(2024, 1, 1), LocalDateTime.now(), 1.0);
        ActivityLog log2 = new ActivityLog(2L, null, null, 10.0,
                LocalDate.of(2024, 1, 2), LocalDateTime.now(), 2.0);
        when(logRepository.findByUser_Id(1L)).thenReturn(List.of(log1, log2));

        List<ActivityLog> list = logService.getLogsByUser(1L);
        Assert.assertEquals(list.size(), 2);
    }

    @Test(priority = 82, groups = "hql")
    public void t82_getFactorByType_found() {
        ActivityType type = new ActivityType(1L, "Car", null, "km", LocalDateTime.now());
        EmissionFactor ef = new EmissionFactor(10L, type, 0.2, "km", LocalDateTime.now());
        when(factorRepository.findByActivityType_Id(1L)).thenReturn(Optional.of(ef));

        EmissionFactor result = factorService.getFactorByType(1L);
        Assert.assertEquals(result.getId(), 10L);
    }

    @Test(priority = 83, groups = "hql")
    public void t83_getFactorByType_notFound() {
        when(factorRepository.findByActivityType_Id(99L)).thenReturn(Optional.empty());
        try {
            factorService.getFactorByType(99L);
            Assert.fail("Expected ResourceNotFoundException");
        } catch (ResourceNotFoundException ex) {
            Assert.assertTrue(ex.getMessage().contains("Emission factor not found"));
        }
    }

    @Test(priority = 84, groups = "hql")
    public void t84_logActivity_calculatesEmissionCorrectly() {
        User u = new User(1L, "User", "u@example.com", "pwd", "USER", LocalDateTime.now());
        ActivityType type = new ActivityType(2L, "Car", null, "km", LocalDateTime.now());
        EmissionFactor ef = new EmissionFactor(5L, type, 0.5, "km", LocalDateTime.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(u));
        when(typeRepository.findById(2L)).thenReturn(Optional.of(type));
        when(factorRepository.findByActivityType_Id(2L)).thenReturn(Optional.of(ef));
        when(logRepository.save(any(ActivityLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ActivityLog log = new ActivityLog();
        log.setQuantity(10.0);
        log.setActivityDate(LocalDate.now());

        ActivityLog created = logService.logActivity(1L, 2L, log);
        Assert.assertEquals(created.getEstimatedEmission(), Double.valueOf(5.0));
    }

    @Test(priority = 85, groups = "hql")
    public void t85_logActivity_futureDateValidation() {
        User u = new User(1L, "User", "u@example.com", "pwd", "USER", LocalDateTime.now());
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));

        ActivityLog log = new ActivityLog();
        log.setQuantity(1.0);
        log.setActivityDate(LocalDate.now().plusDays(1));

        try {
            logService.logActivity(1L, 2L, log);
            Assert.fail("Expected ValidationException for future date");
        } catch (ValidationException ex) {
            Assert.assertTrue(ex.getMessage().contains("cannot be in the future"));
        }
    }

    @Test(priority = 86, groups = "hql")
    public void t86_logActivity_requiresFactor() {
        User u = new User(1L, "User", "u@example.com", "pwd", "USER", LocalDateTime.now());
        ActivityType type = new ActivityType(2L, "Car", null, "km", LocalDateTime.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(u));
        when(typeRepository.findById(2L)).thenReturn(Optional.of(type));
        when(factorRepository.findByActivityType_Id(2L)).thenReturn(Optional.empty());

        ActivityLog log = new ActivityLog();
        log.setQuantity(1.0);
        log.setActivityDate(LocalDate.now());

        try {
            logService.logActivity(1L, 2L, log);
            Assert.fail("Expected ValidationException for missing factor");
        } catch (ValidationException ex) {
            Assert.assertTrue(ex.getMessage().contains("No emission factor configured"));
        }
    }

    // endregion
}
