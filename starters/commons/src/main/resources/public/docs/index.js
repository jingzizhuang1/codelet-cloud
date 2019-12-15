(function() {
  'use strict';

  const templates = {
    endpoints: document.querySelector('#endpoints-template').innerText,
    specifications: document.querySelector('#specifications-template').innerText,
    parameters: document.querySelector('#parameters-template').innerText
  };
  const endpointsPanel = document.querySelector('#endpoints');
  const specificationsPanel = document.querySelector('#specifications');
  const instructionsPanel = document.querySelector('#instructions');
  const httpMethods = ['get', 'post', 'put', 'patch', 'delete'];
  const typeNamePattern = /^(.+?)«(.+?)»$/;
  const propertyOrderPredecessors = [
    'id',
    'name',
    'description'
  ];
  const propertyOrderSuccessors = [
    'revision',
    'createdAt',
    'createdBy',
    'lastModifiedAt',
    'lastModifiedBy',
    'disabledAt',
    'disabledBy',
    'disabled',
    'deletedAt',
    'deletedBy',
    'deleted',
    'pageNo',
    'pageSize',
    'sortBy'
  ];
  const context = {
    $dataTypeNames: {
      object: '对象',
      array: '数组',
      string: '字符串',
      integer: '整数',
      long: '整数',
      boolean: '布尔',
      date: '日期'
    },
    $swagger: null,
    $definitions: {},
    $tags: {
      ['业务参数']: {
        ['业务参数']: {},
      },
      ['通知']: {
        ['通知消息模版']: {},
        ['通知消息模版内容']: {},
        ['电子邮件发送配置']: {},
        ['短信发送配置']: {},
      },
      ['验证码']: {
        ['人机验证码']: {},
        ['验证码配置']: {},
        ['电子邮件及短信验证码']: {},
      },
      ['登录认证']: {
        ['认证凭证']: {},
        ['权限鉴定']: {},
      },
      ['用户']: {
        ['用户']: {},
      },
      ['组织']: {
        ['组织']: {},
      },
      ['职员']: {
        ['职员']: {},
      },
      ['角色与权限']: {
        ['角色组']: {},
        ['角色组成员']: {},
        ['角色权限']: {},
      },
      ['系统']: {
        ['系统']: {},
      },
    },
    $apis: {},
    $endpointCount: 0,
    $refs: {}
  };

  /**
   * Render API documents.
   * @param {Object} swagger
   * @param {[Object]} swagger.tags
   * @param {[Object]} swagger.paths
   * @param {Object} swagger.definitions
   * @param {Number} swagger.count
   * @param {Object} swagger.$tags
   */
  const render = swagger => {
    context.$swagger = swagger;

    // set schema definitions
    Object.keys(swagger.definitions).forEach(typeName => {
      if (typeName.match(typeNamePattern)) {
        return;
      }
      if (context.$definitions[typeName]) {
        return;
      }
      const definition = swagger.definitions[typeName];
      const $definition = context.$definitions[typeName] = {
        type: definition.type,
        title: definition.title,
        properties: []
      };
      Object.keys(definition.properties).forEach(propertyName => {
        const property = definition.properties[propertyName];
        property.name = propertyName;
        property.required = (definition.required || []).indexOf(propertyName) >= 0;
        resolveParameterDescription(property);
        $definition.properties.push(property);
      });
    });

    // set schema definition references
    Object.keys(context.$definitions).forEach(typeName => {
      const $definition = context.$definitions[typeName];
      $definition.properties.forEach(property => {
        !property.properties && resolveParameter(property);
      });
    });

    swagger.$tags = {};
    swagger.tags.forEach(tag => {swagger.$tags[tag.name] = tag.description.split(/\s+/g).join('');});

    // set tags and APIs
    Object.keys(swagger.paths).forEach(reqPath => {
      httpMethods.forEach(reqMethod => {
        if (!swagger.paths[reqPath][reqMethod]) {
          return;
        }

        const api = swagger.paths[reqPath][reqMethod];

        try {
          const constraints = JSON.parse(api.description);
          Object.keys(constraints).forEach(propertyName => {
            api[propertyName] = constraints[propertyName];
          });
        } catch (ignored) {
        }

        if (api.description) {
          api.description = api.description.split(/[\r\n]/g);
        } else {
          api.description = [];
        }

        api.$pathVariableNames = (reqPath.match(/{.+?}/g) || []).map(pathVariableName => pathVariableName.slice(1, -1));
        api.$mappings = [
          {
            method: reqMethod.toUpperCase(),
            path: reqPath,
            pathHTML: reqPath.replace(/{.+?}/g, pathVariable => `<code class="parameter-name">${pathVariable}</code>`),
            variableCount: api.$pathVariableNames.length
          }
        ];

        const tagJSON = api.tags && api.tags[0];
        const tagMap = JSON.parse(tagJSON);
        const tagNames = Object.keys(tagMap);
        let unknownTags = [];

        if (tagNames.length === 0) {
          return;
        }

        tagNames.forEach(tagName => {
          if (tagName === 'domain') {
            api.$domainName = tagMap[tagName];
          } else if (tagName === 'biz') {
            api.$bizName = tagMap[tagName];
          } else if (tagName === 'responsibility') {
            api.$responsibilityName = tagMap[tagName];
          } else if (tagName === 'controller') {
            api.$controllerName = tagMap[tagName];
          } else {
            unknownTags.push(tagName);
          }
        });

        if (!api.$domainName || !api.$bizName || !api.$responsibilityName) {
          const unknownTag = unknownTags[0];
          if (unknownTag) {
            unknownTags = unknownTag.match(/^((.+?)\/)?(.+?)\/(.+?)$/i);
            if (unknownTags) {
              !api.$domainName && (api.$domainName = unknownTags[2] || unknownTags[3]);
              !api.$bizName && (api.$bizName = unknownTags[3]);
              !api.$responsibilityName && (api.$responsibilityName = ({
                query: '查询',
                command: '命令'
              })[unknownTags[4].toLocaleLowerCase()] || unknownTags[4]);
            } else {
              !api.$domainName && (api.$domainName = unknownTag || '其他');
              !api.$bizName && (api.$bizName = unknownTag || '其他');
              !api.$responsibilityName && (api.$responsibilityName = '其他');
            }
          }
        }

        api.$controllerName = (api.$controllerName || swagger.$tags[tagJSON] || '').split('.').map(part => part.match(/^[A-Z]/) ? part : part.charAt(0)).join('.');

        const handlerIdentity = api.operationId.match(/^(class\s+)?(.+?)(<(.+?)>)?\s+(.+?)\((.+?)?\)(_\d+)?$/);
        if (handlerIdentity) {
          const returnType = handlerIdentity[2].split('.').pop();
          const genericType = (handlerIdentity[4] || '').split('.').pop();
          const handlerName = handlerIdentity[5].split('.').pop();
          const parameterTypes = (handlerIdentity[6] || '').split(',').map(parameterType => parameterType.split('.').pop());
          api.$handlerName = `${returnType}${genericType ? `<${genericType}>` : ''} ${handlerName}(${parameterTypes.join(', ')})`;
        }


        api.$handlerId = 'HD' + md5(`${api.$controllerName}#${api.$handlerName}`).toUpperCase();
        api.$pathVariables = [];
        api.$searchParameters = [];
        api.$requestBody = null;

        (api.parameters || []).forEach(parameter => {
          switch (parameter.in) {
            case 'path':
              if (api.$pathVariableNames.length === 0) {
                break;
              }
              resolveParameterDescription(parameter);
              api.$pathVariables.push(resolveParameter(parameter));
              api.$pathVariables.sort((previous, next) => (api.$pathVariableNames.indexOf(previous.name) - api.$pathVariableNames.indexOf(next.name)));
              break;
            case 'query':
              resolveParameterDescription(parameter);
              api.$searchParameters.push(resolveParameter(parameter));
              break;
            case 'body':
              api.$requestBody = resolveParameter(parameter);
              break;
          }
        });

        api.$responseBody = [
          resolveParameter({name: 'success', type: 'boolean', description: '处理是否成功'}),
          resolveParameter({name: 'error', type: 'object', description: '错误信息'}),
          resolveParameter(api.responses['200'] || api.responses['201']),
          resolveParameter({name: 'included', type: 'object', description: '引用数据'})
        ];

        if (api.$responseBody[2]) {
          const responseData = api.$responseBody[2];
          responseData.name = 'data';
          responseData.description = '响应数据';
        } else {
          api.$responseBody.splice(2, 1);
        }

        if (context.$refs[api.$handlerId]) {
          const apiWithSameHandler = context.$refs[api.$handlerId];
          apiWithSameHandler.$mappings.push(api.$mappings[0]);
          api.$pathVariables.forEach(pathVariable => {
            if (apiWithSameHandler.$pathVariableNames.indexOf(pathVariable.name) >= 0) {
              return;
            }
            apiWithSameHandler.$pathVariableNames.push(pathVariable.name);
            apiWithSameHandler.$pathVariables.push(pathVariable);
          });
          apiWithSameHandler.$mappings.sort((previous, next) => {
            if (previous.variableCount !== next.variableCount) {
              return previous.variableCount - next.variableCount;
            }
            return previous.path.localeCompare(next.path);
          });
          return;
        }

        context.$refs[api.$handlerId] = api;
        context.$endpointCount++;

        // domain
        const domain = context.$tags[api.$domainName] = context.$tags[api.$domainName] || {};
        !domain.$id && Object.defineProperties(domain, {
          $id: {value: 'DM' + md5(api.$domainName).toUpperCase()},
          $name: {value: api.$domainName},
          $endpointCount: {value: 0, writable: true}
        });
        domain.$endpointCount++;
        context.$refs[domain.$id] = domain;

        // biz
        const biz = domain[api.$bizName] = domain[api.$bizName] || {};
        !biz.$id && Object.defineProperties(biz, {
          $id: {value: 'BZ' + md5(`${api.$domainName}/${api.$bizName}`).toUpperCase()},
          $name: {value: api.$bizName},
          $domain: {value: domain},
          $endpointCount: {value: 0, writable: true}
        });
        biz.$endpointCount++;
        context.$refs[biz.$id] = biz;

        // responsibility
        const responsibility = biz[api.$responsibilityName] = biz[api.$responsibilityName] || {};
        !responsibility.$id && Object.defineProperties(responsibility, {
          $id: {value: 'RP' + md5(`${api.$domainName}/${api.$bizName}/${api.$responsibilityName}`).toUpperCase()},
          $name: {value: api.$responsibilityName},
          $domain: {value: domain},
          $biz: {value: biz},
          $endpointCount: {value: 0, writable: true}
        });
        responsibility.$endpointCount++;
        context.$refs[responsibility.$id] = responsibility;

        // API
        const uri = reqPath.replace(/{.+?}/g, '{?}');
        Object.defineProperties(api, {
          $id: {value: 'EP' + md5(`${api.$domainName}/${api.$bizName}/${api.$responsibilityName}/${reqMethod}/${uri}`).toUpperCase()},
          $domain: {value: domain},
          $biz: {value: biz},
          $responsibility: {value: responsibility},
          $tags: {value: [api.$domainName]}
        });
        (api.$tags.indexOf(api.$bizName) < 0) && api.$tags.push(api.$bizName);
        (api.$tags.indexOf(api.$responsibilityName) < 0) && api.$tags.push(api.$responsibilityName);
        context.$refs[api.$id] = api;
        responsibility[uri] = responsibility[uri] || {};
        responsibility[uri][reqMethod] = api;
        context.$apis[uri] = context.$apis[uri] ||{};
        context.$apis[uri][reqMethod] = api;
      });
    });

    Object.keys(context.$tags).forEach(domainName => {
      const domain = context.$tags[domainName];
      Object.keys(domain).forEach(bizName => {
        const biz = domain[bizName];
        Object.keys(biz).forEach(responsibilityName => {
          const responsibility = biz[responsibilityName];
          const endpoint = responsibility[Object.keys(responsibility)[0]];
          const api = endpoint[Object.keys(endpoint)[0]];
          !domain.$defaultEndpoint && Object.defineProperty(domain, '$defaultEndpoint', {value: api});
          !biz.$defaultEndpoint && Object.defineProperty(biz, '$defaultEndpoint', {value: api});
          !responsibility.$defaultEndpoint && Object.defineProperty(responsibility, '$defaultEndpoint', {value: api});
        });
      });
    });

    location.href.match(/http:\/\/(127\.0\.0\.1|192\.168\.\d+\.\d+)(:\d+)?\//) && console.debug(context);

    endpointsPanel.innerHTML = ejs.render(templates.endpoints, {context});
    window.onhashchange(null);
  };

  /**
   * Resolve constraints from parameter description.
   * @param parameter
   */
  const resolveParameterDescription = parameter => {
    try {
      const constraints = JSON.parse(parameter.description);
      Object.keys(constraints).forEach(constraintName => {
        parameter[constraintName] = constraints[constraintName];
      });
      if (parameter['typeName'] === 'java.util.Date') {
        parameter['type'] = 'date';
      }
    } catch (ignored) {
    }
  };

  /**
   * Resolve parameter references.
   * @param parameter
   */
  const resolveParameter = parameter => {
    if (!parameter) {
      return null;
    }

    if (parameter.$ref) {
      parameter.$schema = parameter.$ref.slice(14);
    } else if (parameter.items && parameter.items.$ref) {
      parameter.$schema = parameter.items.$ref.slice(14);
    } else if (parameter.schema) {
      if (parameter.schema.$ref) {
        parameter.$schema = parameter.schema.$ref.slice(14);
      } else if (parameter.schema.items && parameter.schema.items.$ref) {
        parameter.$schema = parameter.schema.items.$ref.slice(14);
      }
      parameter.$type = parameter.schema.type || 'object';
    }

    parameter.$type = parameter.type || parameter.$type;
    if (!parameter.$type) {
      return null;
    }

    parameter.$dataTypeName = context.$dataTypeNames[parameter.$type] || parameter.$type;
    if (!parameter.$schema) {
      return parameter;
    }
    if (parameter.$schema.match(typeNamePattern)) {
      const typeInfo = parameter.$schema.match(typeNamePattern);
      if (['List', 'ArrayList', 'Set', 'Collection', 'Page'].indexOf(typeInfo[1]) >= 0) {
        parameter.$type = 'array';
        parameter.$dataTypeName = context.$dataTypeNames[parameter.$type] || parameter.$type;
      }
      parameter.$schema = typeInfo[2];
    }

    parameter.properties = (context.$definitions[parameter.$schema] || {}).properties || [];
    parameter.properties.sort((previous, next) => {
      if (propertyOrderPredecessors.indexOf(previous.name) >= 0 && propertyOrderPredecessors.indexOf(next.name) >= 0) {
        return propertyOrderPredecessors.indexOf(previous.name) - propertyOrderPredecessors.indexOf(next.name);
      }
      if (propertyOrderPredecessors.indexOf(previous.name) < 0) {
        return 1;
      }
      if (propertyOrderPredecessors.indexOf(next.name) < 0) {
        return -1;
      }
    });
    parameter.properties.sort((previous, next) =>
      propertyOrderSuccessors.indexOf(previous.name) - propertyOrderSuccessors.indexOf(next.name)
    );

    return parameter;
  };

  /**
   * Render API document.
   * @param api
   */
  const renderDoc = api => {
    if (!api) {
      document.body.classList.remove('specifications');
      specificationsPanel.innerHTML = '';
      return;
    }
    specificationsPanel.innerHTML = ejs.render(templates.specifications, {templates, context, api});
    document.body.classList.add('specifications');
  };

  let expandIndexTimeout;
  endpointsPanel.addEventListener('mouseenter', event => {
    expandIndexTimeout = setTimeout(() => {
      document.body.classList.add('expand-index');
    }, 150);
  });
  endpointsPanel.addEventListener('mouseleave', event => {
    clearTimeout(expandIndexTimeout);
    document.body.classList.remove('expand-index');
  });
  specificationsPanel.addEventListener('mouseenter', event => {
    clearTimeout(expandIndexTimeout);
    document.body.classList.remove('expand-index');
  });
  instructionsPanel.addEventListener('mouseenter', event => {
    clearTimeout(expandIndexTimeout);
    document.body.classList.remove('expand-index');
  });

  window.onhashchange = () => {
    const matched = location.hash.match(/^#\/(.+?)$/);
    if (!matched) {
      document.body.classList.remove('show-index');
      document.body.classList.remove('specifications');
      return;
    }

    const targetId = matched[1];
    let target = context.$refs[targetId];

    if (!target) {
      document.body.classList.remove('show-index');
      document.body.classList.remove('specifications');
      return;
    }

    if (target.$defaultEndpoint) {
      target = target.$defaultEndpoint;
    } else {
      document.body.classList.remove('show-index');
    }

    const expanded = document.querySelectorAll('nav li.expanded');
    expanded.forEach(element => {
      element.classList.remove('expanded');
    });

    document.querySelector(`#${target.$id}`).classList.add('expanded');
    document.querySelector(`#${target.$domain.$id}`).classList.add('expanded');

    renderDoc(target);
  };

  const xhr = new XMLHttpRequest();
  xhr.open('GET', '/docs/swagger.json');
  xhr.addEventListener('load', () => {render(JSON.parse(xhr.responseText));});
  xhr.send();

  console.log(
    '%cCodelet Cloud%c\nnot only technology, but also best practices.',
    'font-size: 32px; font-family: "Verdana", "Tahoma", "sans-serif"; text-shadow: 0 -1px #000000; padding: 0 .5em 0 .5em; line-height: 1.5em; background: linear-gradient(#007FFF, #003F7F); font-weight: bold; color: #FFFFFF;',
    'font-size: 16px; font-family: "Verdana", "Tahoma", "sans-serif"; text-shadow: 0 -1px #000000; padding: 0 .5em 0 .5em; line-height: 1.5em; background: #003F7F;'
  );
  console.log('http://codelet.net');
})();
