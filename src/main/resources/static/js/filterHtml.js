function $filterHtml(html, opts) {
    var _html = $("<div>" + html + "</div>"), htmlText = "", options = {
        badAttr:[ /^on/ ],
        whiteHrefScheme:[ "http", "https", "tel" ],
        whiteSrcScheme:[ "http", "https", "tel" ],
        badStyleName:[ "behavior" ],
        badStyleVal:[ "expression" ],
        badTag:[ "html", "body", "head", "meta", "title", "script", "iframe", "object", "embed", "applet", "video", "audio", "canvas" ],
        isClearCssImport:true
    };
    option = $.extend({}, options, opts);
    $.each(_html.find("*") || [], function(i, items) {
        //禁止所有 on 开头的属性
        var _item = $(items), href = "", src = "", formAction = "", scheme = "", isBadTag = false;
        /*if( !items || $.inArray( items.nodeType, [1, 11]) == -1 ){
         return ;
         }*/
        //去掉 badTag
        $.each(option.badTag, function(i, tag) {
            if (_item.is(tag)) {
                _item.remove();
                isBadTag = true;
                return;
            }
        });
        //如果是 坏标签，直接进入下一次循环
        if (isBadTag) {
            return;
        }
        //获取所有属性
        $.each(items.attributes || [], function(i, attrs) {
            if (!attrs) {
                return;
            }
            var attrName = attrs.name || attrs;
            //去掉属性
            $.each(option.badAttr, function(i, attr) {
                if (attrName.match(attr)) {
                    _item.removeAttr(attrName);
                }
            });
            //检测 white Href Scheme
            if ("href" == attrName) {
                href = (_item.attr("href") || "").split(":");
                //数组
                scheme = href[0] || "";
                //如果 scheme 不在白名单
                if ($.inArray(scheme, option.whiteHrefScheme) == -1) {
                    _item.attr("href", "#");
                }
            }
            //检测 white src Scheme
            if ("src" == attrName) {
                src = (_item.attr("src") || "").split(":");
                //数组
                scheme = src[0] || "";
                //如果 scheme 不在白名单
                if ($.inArray(scheme, option.whiteSrcScheme) == -1) {
                    _item.attr("src", "");
                }
            }
        });
        //检测 from
        if ("FORM" == items.tagName) {
            formAction = (_item.attr("action") || "").split(":");
            //数组
            scheme = formAction[0] || "";
            //如果 scheme 不在白名单
            if ($.inArray(scheme, option.whiteSrcScheme) == -1) {
                _item.attr("action", "");
            }
        }
        //去掉badStyleName
        $.each(option.badStyleName, function(i, name) {
            items.style[name] = "";
        });
        //去掉 badStyleVal
        $.each((_item.attr("style") || "").split(";") || [], function(i, styleCss) {
            styleCss = (styleCss || "").split(":") || [];
            var name = styleCss[0] || "", val = styleCss[1] || "";
            //检测 坏值，只要发现有问题的 值，则把整个 名 清空
            $.each(option.badStyleVal || [], function(i, badVal) {
                if (val.toLowerCase().indexOf(badVal.toLowerCase()) > -1) {
                    items.style[name] = "";
                }
            });
        });
        //检测 background 的 url，防止在此处引入 js： background:url(javascript:xxxxxx);
        //此方法 支持： background : ; background-image 同时出现的情况
        (function() {
            var url = $.trim(items.style.backgroundImage || ""), image = "", scheme = "";
            if (url) {
                //取出图片地址
                url = url.match(/url\s*\(['"\s]*([^\)]*)['"]*\)/) || [];
                image = (url[1] || "").split(":");
            }
            //允许 url( );
            //判断 url scheme
            if (image.length > 0) {
                scheme = image[0] || "";
                //如果 scheme 不在白名单
                if ($.inArray(scheme, option.whiteSrcScheme) == -1) {
                    items.style.backgroundImage = "";
                }
            }
        })();
        //是否清理  @import url("CssStyle.css");  @import "css_red.css";
        if (option.isClearCssImport && "STYLE" == items.tagName) {
            //兼容 ie
            _item.after('<style type="text/css">' + _item.html().replace(/@import\s/gi, "@WANG ") + "</style>");
            _item.remove();
        }
    });
    htmlText = _html.html();
    //是否出现过不被允许的节点。 对孤立标签的解析，各个浏览器有差异，这里直接作为字符串替换
    //去掉孤立的<b>, </b>, </b style="">, </b
    var badTagReg = "";
    $.each(option.badTag, function(i, tag) {
        badTagReg += "<" + tag + "[^>]*>|</" + tag + "[^>]*>?";
    });
    if (badTagReg) {
        htmlText = htmlText.replace(new RegExp(badTagReg, "ig"), "");
    }
    return htmlText;
}
