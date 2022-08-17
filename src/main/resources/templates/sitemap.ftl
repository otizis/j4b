<?xml version="1.0" encoding="UTF-8"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
    <#if pageList ??>
        <#list pageList as page>
            <url>
                <loc>${blogDomain}/page/${page.id}</loc>
                <lastmod>${page.updateAt?date}</lastmod>
            </url>
        </#list>
    </#if>
</urlset>
