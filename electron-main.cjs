const { app, BrowserWindow, shell, session, ipcMain } = require('electron');
const path = require('path');

// Prevent white screen / GPU glitching on some Windows 11 drivers if hardware acceleration fails
app.disableHardwareAcceleration();

function createWindow() {
  const mainWindow = new BrowserWindow({
    width: 1280,
    height: 820,
    minWidth: 900,
    minHeight: 600,
    title: 'ISTEK BROWSER',
    show: false, // Don't show until ready-to-show to prevent initial white flash
    backgroundColor: '#020617', // Match dark slate theme
    webPreferences: {
      nodeIntegration: false,
      contextIsolation: true,
      webSecurity: false,
      allowRunningInsecureContent: true,
      webviewTag: true,
    },
    autoHideMenuBar: true,
  });

  // Set Windows 11/10 Google Chrome User Agent so YouTube and web services identify app as standard Google Chrome
  const chromeUserAgent =
    'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36';
  mainWindow.webContents.setUserAgent(chromeUserAgent);

  // Configure session request handler to allow web embedding & YouTube playback
  session.defaultSession.webRequest.onHeadersReceived((details, callback) => {
    const responseHeaders = Object.assign({}, details.responseHeaders);
    delete responseHeaders['x-frame-options'];
    delete responseHeaders['X-Frame-Options'];
    delete responseHeaders['content-security-policy'];
    delete responseHeaders['Content-Security-Policy'];
    callback({ responseHeaders });
  });

  const indexPath = path.join(__dirname, 'dist', 'index.html');

  // Show window smoothly when rendered to avoid white screen flash
  mainWindow.once('ready-to-show', () => {
    mainWindow.show();
  });

  mainWindow.loadFile(indexPath).catch((err) => {
    console.error('Failed to load local HTML file, falling back:', err);
    mainWindow.loadURL('http://localhost:3000');
  });

  // Handle load errors automatically so user never gets stuck on white screen
  mainWindow.webContents.on('did-fail-load', (event, errorCode, errorDescription) => {
    console.warn('Page load failed:', errorCode, errorDescription);
    if (errorCode !== -3) { // ignore aborted loads
      mainWindow.loadFile(indexPath).catch(() => {});
    }
  });

  // Handle popups / new window requests cleanly inside browser
  mainWindow.webContents.setWindowOpenHandler(({ url }) => {
    if (url.startsWith('http')) {
      shell.openExternal(url);
    }
    return { action: 'deny' };
  });
}

app.whenReady().then(() => {
  createWindow();

  app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) createWindow();
  });
});

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') app.quit();
});
