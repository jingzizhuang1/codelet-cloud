package net.codelet.cloud.repository;

import net.codelet.cloud.annotation.SearchPredicate;
import net.codelet.cloud.dto.BaseDTO;
import net.codelet.cloud.dto.PaginationDTO;
import net.codelet.cloud.dto.SortDTO;
import net.codelet.cloud.entity.BaseEntity;
import net.codelet.cloud.entity.BaseVersionedEntity;
import net.codelet.cloud.error.ConflictError;
import net.codelet.cloud.error.NotFoundError;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.criteria.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static net.codelet.cloud.annotation.SearchPredicate.*;

/**
 * 数据仓库基础类。
 * @param <T> 实体的范型
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends PagingAndSortingRepository<T, String>, JpaSpecificationExecutor<T> {

    /**
     * 批量取得实体。
     * @param entityIDs 实体 ID 集合
     * @return 实体列表
     */
    List<T> findByIdIn(Collection<String> entityIDs);

    /**
     * 是否存在满足查询条件的实体。
     * @param queryDTO 查询条件参数
     * @param <Q>      查询条件参数范型
     * @return 是否存在满足查询条件的实体
     */
    default <Q extends BaseDTO> boolean existsByCriteria(Q queryDTO) {
        return findOneByCriteria(queryDTO).isPresent();
    }

    /**
     * 是否存在满足查询条件的实体。
     * @param queryDTO 查询条件参数
     * @param revision 修订版本号
     * @param <Q>      查询条件参数范型
     * @return 是否存在满足查询条件的实体
     */
    default <Q extends BaseDTO> boolean existsByCriteria(Q queryDTO, Long revision) {
        try {
            return findOneByCriteria(queryDTO, revision).isPresent();
        } catch (NotFoundError | ConflictError e) {
            return false;
        }
    }

    /**
     * 取得一件满足查询条件的实体。
     * @param queryDTO 查询条件参数
     * @param <Q>      查询条件参数范型
     * @return 满足查询条件的实体
     */
    default <Q extends BaseDTO> Optional<T> findOneByCriteria(Q queryDTO) {
        return findOne(buildCriteria(queryDTO, false));
    }

    /**
     * 取得一件满足查询条件的实体。
     * @param queryDTO 查询条件参数
     * @param deleted  是否已被删除
     * @param <Q>      查询条件参数范型
     * @return 满足查询条件的实体
     */
    default <Q extends BaseDTO> Optional<T> findOneByCriteria(Q queryDTO, Boolean deleted) {
        return findOne(buildCriteria(queryDTO, deleted));
    }

    /**
     * 取得一件满足查询条件的实体。
     * 若不存在满足条件的实体，则将抛出 NotFoundError；
     * 若存在满足条件的实体，但修订版本号不同，则将抛出 ConflictError。
     * @param queryDTO 查询条件参数
     * @param revision 修订版本号
     * @param <Q>      查询条件参数范型
     * @return 满足查询条件的实体
     */
    default <Q extends BaseDTO> Optional<T> findOneByCriteria(Q queryDTO, Long revision) {
        return findOneByCriteria(queryDTO, false, revision);
    }

    /**
     * 取得一件满足查询条件的实体。
     * 若不存在满足条件的实体，则将抛出 NotFoundError；
     * 若存在满足条件的实体，但修订版本号不同，则将抛出 ConflictError。
     * @param queryDTO 查询条件参数
     * @param deleted  是否已被删除
     * @param revision 修订版本号
     * @param <Q>      查询条件参数范型
     * @return 满足查询条件的实体
     */
    default <Q extends BaseDTO> Optional<T> findOneByCriteria(Q queryDTO, Boolean deleted, Long revision) {
        Optional<T> optional = findOne(buildCriteria(queryDTO, deleted, "revision"));
        T entity = optional.orElse(null);
        if (entity == null) {
            throw new NotFoundError();
        }
        if (revision != null
            && (entity instanceof BaseVersionedEntity)
            && !revision.equals(((BaseVersionedEntity) entity).getRevision())) {
            throw new ConflictError();
        }
        return optional;
    }

    /**
     * 取得所有满足查询条件的数据实体的列表。
     * 设置为 null 的查询条件将被忽略。
     * @param queryDTO 查询条件参数
     * @param <Q>      查询条件参数范型
     * @return 查询结果列表
     */
    default <Q extends BaseDTO> List<T> findAllByCriteria(Q queryDTO) {
        return findAllByCriteria(queryDTO, false);
    }

    /**
     * 取得所有满足查询条件的数据实体的列表。
     * 设置为 null 的查询条件将被忽略。
     * @param queryDTO 查询条件参数
     * @param deleted  是否已被删除
     * @param <Q>      查询条件参数范型
     * @return 查询结果列表
     */
    default <Q extends BaseDTO> List<T> findAllByCriteria(Q queryDTO, Boolean deleted) {
        return findAll(buildCriteria(queryDTO, deleted));
    }

    /**
     * 取得所有满足查询条件的数据实体的分页数据。
     * 设置为 null 的查询条件将被忽略。
     * @param queryDTO 查询条件参数
     * @param <Q>      查询条件参数范型
     * @return 查询结果分页数据
     */
    default <Q extends BaseDTO> Page<T> findAllByCriteria(Q queryDTO, Pageable pageable) {
        return findAllByCriteria(queryDTO, false, pageable);
    }

    /**
     * 取得所有满足查询条件的数据实体的分页数据。
     * 设置为 null 的查询条件将被忽略。
     * @param queryDTO 查询条件参数
     * @param deleted  是否已被删除
     * @param <Q>      查询条件参数范型
     * @return 查询结果分页数据
     */
    default <Q extends BaseDTO> Page<T> findAllByCriteria(Q queryDTO, Boolean deleted, Pageable pageable) {
        return findAll(buildCriteria(queryDTO, deleted), pageable);
    }

    // 默认查询条件配置
    SearchPredicate DEFAULT_SEARCH_PREDICATE_CONFIGURATION = new SearchPredicate() {

        @Override
        public Class<? extends Annotation> annotationType() {
            return SearchPredicate.class;
        }

        @Override
        public Condition value() {
            return Condition.EQUAL;
        }

        @Override
        public String[] propertyNames() {
            return new String[0];
        }

        @Override
        public IgnoreValue ignoreValue() {
            return IgnoreValue.NULL;
        }

        @Override
        public LikePosition like() {
            return LikePosition.START;
        }

        @Override
        public boolean ignoreCase() {
            return false;
        }
    };

    /**
     * 根据查询条件参数（PaginationDTO 的子类型）生成查询条件（Predicate 的列表）。
     * 设置为 null 的查询条件将被忽略。
     * @param queryDTO         查询条件参数
     * @param deleted          是否已被删除
     * @param ignoreProperties 略过的属性
     * @param <Q>              查询条件参数的范型
     * @param <E>              数据实体的范型
     * @return 查询条件
     */
    static <Q extends BaseDTO, E extends BaseEntity> Specification<E> buildCriteria(
        Q queryDTO,
        Boolean deleted,
        String... ignoreProperties
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Set<String> resolvedPropertyNames = setPredicates(
                root,
                criteriaBuilder,
                predicates,
                queryDTO,
                queryDTO.getClass(),
                ignoreProperties
            );

            // 设置是否已删除的查询条件
            try {
                if (deleted != null
                    && !resolvedPropertyNames.contains("deleted")
                    && root.get("deleted") != null) {
                    predicates.add(
                        criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("deleted"), deleted)
                        )
                    );
                }
            } catch (IllegalArgumentException ignored) {
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
        };
    }

    /**
     * 根据查询条件参数（PaginationDTO 的子类型）生成查询条件（Predicate 的列表）。
     * 设置为 null 的查询条件将被忽略。
     * @param root             查询语句中 FROM 子句的根类型（即数据实体的类型）
     * @param criteriaBuilder  查询条件构造器
     * @param predicates       查询条件列表
     * @param queryDTO         查询条件参数
     * @param type             查询条件参数的类型
     * @param ignoreProperties 略过的属性
     * @param <Q>              查询条件参数的范型
     * @param <E>              数据实体的范型
     * @return 设置了条件的属性名的集合
     */
    static <Q extends BaseDTO, E extends BaseEntity> Set<String> setPredicates(
        Root<E> root,
        CriteriaBuilder criteriaBuilder,
        List<Predicate> predicates,
        Q queryDTO,
        Class<?> type,
        String... ignoreProperties
    ) {
        return setPredicates(
            root,
            criteriaBuilder,
            predicates,
            queryDTO,
            type,
            new HashSet<>(Arrays.asList(ignoreProperties)),
            new HashSet<>()
        );
    }

    /**
     * 根据查询条件参数（PaginationDTO 的子类型）生成查询条件（Predicate 的列表）。
     * @see   SearchPredicate
     * @param root             查询语句中 FROM 子句的根类型（即数据实体的类型）
     * @param criteriaBuilder  查询条件构造器
     * @param predicates       查询条件列表
     * @param queryDTO         查询条件参数
     * @param queryDtoType     查询条件参数的类型
     * @param ignoreProperties 略过的属性
     * @param <Q>              查询条件参数的范型
     * @param <E>              数据实体的范型
     * @return 设置了条件的属性名的集合
     */
    static <Q extends BaseDTO, E extends BaseEntity> Set<String> setPredicates(
        Root<E> root,
        CriteriaBuilder criteriaBuilder,
        List<Predicate> predicates,
        Q queryDTO,
        Class<?> queryDtoType,
        Set<String> ignoreProperties,
        Set<String> resolvedPropertyNames
    ) {
        // 不对分页参数或排序参数进行处理
        if (queryDtoType == null
            || queryDtoType == PaginationDTO.class
            || queryDtoType == SortDTO.class) {
            return resolvedPropertyNames;
        }

        // 处理超类的属性
        setPredicates(
            root,
            criteriaBuilder,
            predicates,
            queryDTO,
            queryDtoType.getSuperclass(),
            ignoreProperties,
            resolvedPropertyNames
        );

        for (Field field : queryDtoType.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            field.setAccessible(true);

            try {
                Object value = field.get(queryDTO);

                SearchPredicate configuration = field.getAnnotation(SearchPredicate.class);
                if (configuration == null) {
                    configuration = DEFAULT_SEARCH_PREDICATE_CONFIGURATION;
                }

                // 若指定的值被设置为忽略则不进行处理
                if (((configuration.ignoreValue() == IgnoreValue.NULL && value == null)
                    || (configuration.ignoreValue() == IgnoreValue.EMPTY_STRING && "".equals(value))
                    || (configuration.ignoreValue() == IgnoreValue.ZERO && Integer.valueOf(0).equals(value)))) {
                    continue;
                }

                // 取得设置条件的属性名
                Set<String> propertyNames = new HashSet<>(
                    configuration.propertyNames().length == 0
                        ? Collections.singletonList(field.getName())
                        : Arrays.asList(configuration.propertyNames())
                );
                propertyNames.removeAll(ignoreProperties);

                // 若属性全部被忽略或为比较运算符但设置的值不可比较则不进行处理
                if (propertyNames.size() == 0
                    || (configuration.value().isNumeric() && !(value instanceof Comparable))) {
                    continue;
                }

                resolvedPropertyNames.addAll(propertyNames);

                // 判断单个属性时添加对该属性的判断条件（AND ...）
                if (propertyNames.size() == 1) {
                    predicates.add(
                        criteriaBuilder.and(
                            setPredicates(
                                root,
                                criteriaBuilder,
                                configuration,
                                propertyNames.toArray(new String[] {})[0],
                                value
                            )
                        )
                    );
                // 判断多个属性时创建对这些属性的或关系的条件（AND (... OR ...)）
                } else {
                    List<Predicate> subPredicates = new ArrayList<>();
                    for (String propertyName : propertyNames) {
                        subPredicates.add(
                            setPredicates(
                                root,
                                criteriaBuilder,
                                configuration,
                                propertyName,
                                value
                            )
                        );
                    }
                    predicates.add(
                        criteriaBuilder.and(
                            criteriaBuilder.or(subPredicates.toArray(new Predicate[] {}))
                        )
                    );
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace(System.err);
            }
        }

        return resolvedPropertyNames;
    }

    /**
     * 生成查询属性的查询条件。
     * @see   SearchPredicate
     * @param root            查询语句中 FROM 子句的根类型（即数据实体的类型）
     * @param criteriaBuilder 查询条件构造器
     * @param configuration   查询条件配置
     * @param propertyName    条件判断属性名
     * @param value           查询条件属性值
     * @param <E>             查询数据实体类型范型
     * @return 查询条件
     */
    @SuppressWarnings("unchecked")
    static <E extends BaseEntity> Predicate setPredicates(
        Root<E> root,
        CriteriaBuilder criteriaBuilder,
        SearchPredicate configuration,
        String propertyName,
        Object value
    ) {
        Predicate predicate = null;
        Expression expression = root.get(propertyName);

        if (configuration.ignoreCase() && value instanceof String) {
            expression = criteriaBuilder.upper(expression);
            value = ((String) value).toUpperCase();
        }

        switch (configuration.value()) {
            case EQUAL:
                // field IS NULL
                if (value == null) {
                    predicate = criteriaBuilder.isNull(expression);
                // field IN (item1, item2, ...)
                } else if (value instanceof Collection) {
                    predicate = criteriaBuilder.in(expression);
                    for (Object item : (Collection) value) {
                        ((CriteriaBuilder.In<Object>) predicate).value(item);
                    }
                // field = value
                } else {
                    predicate = criteriaBuilder.equal(expression, value);
                }
                return predicate;
            case NOT_EQUAL:
                // field IS NOT NULL
                if (value == null) {
                    predicate = criteriaBuilder.isNotNull(expression);
                // NOT (field IN (item1, item2, ...))
                } else if (value instanceof Collection) {
                    predicate = criteriaBuilder.in(expression);
                    for (Object item : (Collection) value) {
                        ((CriteriaBuilder.In<Object>) predicate).value(item);
                    }
                    predicate = criteriaBuilder.not(predicate);
                // field <> value
                } else {
                    predicate = criteriaBuilder.notEqual(expression, value);
                }
                return predicate;
            case GREATER_THAN:
                // field > value
                return criteriaBuilder.greaterThan(expression, (Comparable) value);
            case GREATER_THAN_OR_EQUAL_TO:
                // field >= value
                return criteriaBuilder.greaterThanOrEqualTo(expression, (Comparable) value);
            case LESS_THAN:
                // field < value
                return criteriaBuilder.lessThan(expression, (Comparable) value);
            case LESS_THAN_OR_EQUAL_TO:
                // field <= value
                return criteriaBuilder.lessThanOrEqualTo(expression, (Comparable) value);
            case LIKE:
                // field LIKE 'value%'
                return criteriaBuilder.like(expression, configuration.like().pattern((String) value));
            case NOT_LIKE:
                // NOT (field LIKE 'value%')
                return criteriaBuilder.notLike(expression, configuration.like().pattern((String) value));
        }

        return predicate;
    }
}
