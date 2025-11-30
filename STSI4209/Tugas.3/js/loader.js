(async function() {
    const templates = [
        { id: 'tpl-stock', url: 'templates/stock-table.html' },
        { id: 'tpl-tracking', url: 'templates/do-tracking.html' },
        { id: 'tpl-order', url: 'templates/order-form.html' },
        { id: 'tpl-badge', url: 'templates/status-badge.html' },
        { id: 'tpl-modal', url: 'templates/app-modal.html' }
    ];

    const loadTemplate = async (tmpl) => {
        try {
            const response = await fetch(tmpl.url);
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
            const html = await response.text();
            const script = document.createElement('script');
            script.type = 'text/x-template';
            script.id = tmpl.id;
            script.innerHTML = html;
            document.body.appendChild(script);
            console.log(`Loaded template: ${tmpl.id}`);
        } catch (e) {
            console.error(`Failed to load template ${tmpl.url}`, e);
        }
    };

    console.log('Starting template loading...');
    await Promise.all(templates.map(loadTemplate));
    console.log('All templates loaded.');

    // Now load the component scripts
    const scripts = [
        'js/services/api.js',
        'js/components/status-badge.js',
        'js/components/app-modal.js',
        'js/components/stock-table.js',
        'js/components/do-tracking.js',
        'js/components/order-form.js',
        'js/app.js'
    ];

    const loadScript = (src) => {
        return new Promise((resolve, reject) => {
            const script = document.createElement('script');
            script.src = src;
            script.async = false; 
            script.onload = resolve;
            script.onerror = reject;
            document.body.appendChild(script);
        });
    };

    // Load scripts sequentially to ensure dependencies (like ApiService) are ready if needed
    // Although Vue components register globally, app.js must be last.
    for (const src of scripts) {
        await loadScript(src);
        console.log(`Loaded script: ${src}`);
    }
})();