-- -----------------------------------------------------------------------------
-- Codelet Cloud 存储过程创建脚本
-- 数据库：MySQL Server 5.7
-- 模　块：组织
-- -----------------------------------------------------------------------------
USE `codelet_cloud_organization`;

DELIMITER !!

-- -----------------------------------------------------------------------------
-- 逻辑删除无任何上级的组织。
-- -----------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS `delete_organizations_with_no_parent`!!
CREATE PROCEDURE `delete_organizations_with_no_parent`(
    IN  operator_id   VARCHAR(16), -- 操作者用户 ID
    IN  company_id    VARCHAR(16), -- 公司 ID
    OUT deleted_total INTEGER      -- 删除组织数据件数
)
BEGIN
    DECLARE organization_id VARCHAR(16);
    DECLARE deleted_count   INTEGER DEFAULT 0;

    -- 查询所有无上级组织的组织
    DECLARE done         INTEGER DEFAULT 0;
    DECLARE fetch_cursor CURSOR FOR SELECT `id` FROM `organization_with_no_parent` WHERE `company_id` = company_id;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    IF deleted_total IS NULL THEN
        SET deleted_total = 0;
    END IF;

    -- 取得每一个无上级组织的组织，并删除其组织信息及所有层级关系数据
    OPEN fetch_cursor;
    fetch_organization: REPEAT
        -- 尝试取得无上级组织的组织的 ID
        FETCH fetch_cursor INTO organization_id;

        -- 若未能取得数据则结束查询
        IF done THEN
            LEAVE fetch_organization;
        END IF;

        -- 更新已删除组织数据件数
        SET deleted_total = deleted_total + 1;

        -- 逻辑删除组织信息
        UPDATE
            `organization`
        SET
            `revision`   = UNIX_TIMESTAMP() * 1000,
            `deleted`    = 1,
            `deleted_at` = CURRENT_TIMESTAMP(),
            `deleted_by` = operator_id
        WHERE
            `id` = organization_id
        ;

        -- 逻辑删除组织的所有关系数据
        UPDATE
            `organization_hierarchy`
        SET
            `revision`   = UNIX_TIMESTAMP() * 1000,
            `deleted`    = 1,
            `deleted_at` = CURRENT_TIMESTAMP(),
            `deleted_by` = operator_id
        WHERE
            (`org_id` = organization_id OR `parent_id` = organization_id)
        AND `deleted` = 0
        ;
    UNTIL done END REPEAT;
    CLOSE fetch_cursor;

    -- 若存在被删除的组织，则继续当前处理
    IF deleted_total > 0 THEN
        CALL `delete_organizations_with_no_parent`(operator_id, company_id, deleted_count);
        SET deleted_total = deleted_total + deleted_count;
    END IF;
END!!

DELIMITER ;
