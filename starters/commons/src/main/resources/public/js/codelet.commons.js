/*!
 * Codelet Cloud Commons.
 * Copyright (c) 2019 CodeDance
 */

window.$CC = window.$CC || {};

/**
 * JSON API 数据结构解析函数。
 * 浏览器兼容性：
 *  - IE       9
 *  - Edge    12
 *  - Chrome   5
 *  - Safari   5.1
 *  - Firefox  4
 *  - Opera   12
 * @type {Function}
 */
window.$CC.resolveJsonApi = (function() {
  'use strict';

  return resolveJsonApi;

  /**
   * 解析 JSON API 形式的 HTTP 响应数据。
   * @param {{data, included}} jsonApi HTTP 响应数据对象
   * @returns {object} 解析后的数据对象
   */
  function resolveJsonApi(jsonApi) {
    if (!jsonApi || !jsonApi.data || !isObject(jsonApi.included)) {
      return jsonApi;
    }
    resolve(jsonApi.data, jsonApi.included);
    return jsonApi;
  }

  /**
   * 解析数据对象。
   * @param {*}      context  JSON API 中的数据对象或数据对象的数组
   * @param {object} included 引用数据 ID 与数据对象的映射表
   */
  function resolve(context, included) {
    if (!context) {
      return;
    }

    // 当为数组时解析每一个元素
    if (Array.isArray(context)) {
      context.forEach(function(item) {
        resolve(item, included);
      });
      return;
    }

    // 若不为对象或已解析则不做处理
    if (!isObject(context) || context['$resolved']) {
      return;
    }

    // 将对象标记为已解析
    Object.defineProperty(context, '$resolved', {value: true});

    Object.keys(context).forEach(function(propertyName) {
      var property = context[propertyName];

      if (!property) {
        return;
      }

      var entity;

      // 若为实体 ID 则合并对象属性
      if (propertyName === '$ref'
          && typeof property === 'string'
          && (entity = included[property])) {
        delete context['$ref'];
        Object.defineProperty(context, '$ref', {value: property});
        Object.keys(entity).forEach(function(propertyName) {
          if (context[propertyName] !== null
              && typeof context[propertyName] !== 'undefined') {
            return;
          }
          context[propertyName] = entity[propertyName];
          resolve(context[propertyName], included);
        });
      // 否则解析属性
      } else {
        resolve(property, included);
      }
    });
  }

  /**
   * 判断给定的值是否为对象。
   * @param {*} value 判断对象
   * @returns {boolean} 是否为对象
   */
  function isObject(value) {
    return Object.prototype.toString.call(value) === '[object Object]';
  }
})();
