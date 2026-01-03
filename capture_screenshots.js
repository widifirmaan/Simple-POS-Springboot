
const puppeteer = require('puppeteer');

(async () => {
    console.log('Starting screenshot capture v2...');
    let browser;
    try {
        browser = await puppeteer.launch({
            headless: "new",
            args: ['--no-sandbox', '--disable-setuid-sandbox', '--disable-dev-shm-usage']
        });
        const page = await browser.newPage();
        await page.setViewport({ width: 1280, height: 1024 });

        const baseUrl = 'http://localhost:8081';

        // 1. Seed data
        console.log('Seeding data...');
        try {
            const seedResponse = await page.goto(`${baseUrl}/seed`);
            console.log('Seed response status:', seedResponse.status());
            const seedText = await seedResponse.text();
            console.log('Seed response text:', seedText);
        } catch (err) {
            console.log('Seeding failed (maybe app not ready?):', err.message);
        }
        await new Promise(r => setTimeout(r, 2000));

        // 2. Login
        console.log('Capturing Login...');
        await page.goto(`${baseUrl}/`, { waitUntil: 'networkidle2' });
        await page.waitForSelector('input[name="username"]');
        await page.screenshot({ path: 'screenshots/login.png' });

        console.log('Logging in...');
        await page.type('input[name="username"]', 'hamam', { delay: 100 });
        await page.type('input[name="password"]', '123', { delay: 100 });

        // Click submit button
        const submitBtn = await page.$('button[type="submit"]');
        if (submitBtn) {
            await Promise.all([
                page.waitForNavigation({ waitUntil: 'networkidle2', timeout: 10000 }),
                submitBtn.click()
            ]).catch(e => console.log('Navigation or click timeout/error:', e.message));
        } else {
            console.log('Submit button not found, pressing Enter');
            await page.keyboard.press('Enter');
            await page.waitForNavigation({ waitUntil: 'networkidle2' }).catch(e => console.log(e.message));
        }

        console.log('Current URL after login:', page.url());

        // 3. Dashboard
        console.log('Capturing Dashboard...');
        // Log if we are not at dashboard
        if (!page.url().includes('dashboard') && !page.url().includes('localhost:8081/')) {
            // Maybe redirected somewhere else?
            console.log('Warning: Not at dashboard, forcing navigation.');
            await page.goto(`${baseUrl}/dashboard`, { waitUntil: 'networkidle2' });
        } else if (page.url().endsWith('/')) {
            // Still at index?
            console.log('Warning: Still at index (login failed?). checking for error.');
            const errorEl = await page.$('.alert-danger');
            if (errorEl) {
                const errText = await page.evaluate(el => el.textContent, errorEl);
                console.log('Login Error Message:', errText);
            }
        }

        await page.screenshot({ path: 'screenshots/dashboard.png' });

        // 4. Kasir
        console.log('Capturing Kasir...');
        await page.goto(`${baseUrl}/kasir`, { waitUntil: 'networkidle2' });
        await page.screenshot({ path: 'screenshots/kasir.png' });

        // 5. Stock Barang
        console.log('Capturing Stock Barang...');
        await page.goto(`${baseUrl}/stock-barang`, { waitUntil: 'networkidle2' });
        await page.screenshot({ path: 'screenshots/stock.png' });

        // 6. Bill
        console.log('Capturing Bill...');
        await page.goto(`${baseUrl}/bill`, { waitUntil: 'networkidle2' });
        await page.screenshot({ path: 'screenshots/bill.png' });

        // 7. Finance
        console.log('Capturing Finance...');
        await page.goto(`${baseUrl}/finance`, { waitUntil: 'networkidle2' });
        await page.screenshot({ path: 'screenshots/finance.png' });

        console.log('Done!');
    } catch (e) {
        console.error('Error in script:', e);
    } finally {
        if (browser) await browser.close();
    }
})();
