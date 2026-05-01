// logo.js — reusable NPN transistor logo component
// Usage: <script src="/logo.js"></script>
// Then use: <lab-logo></lab-logo> anywhere in your HTML

class LabLogo extends HTMLElement {
  connectedCallback() {
    const size = this.getAttribute('size') || '40';
    const showName = this.getAttribute('show-name') !== 'false';
    const dark = this.getAttribute('dark') === 'true';
    const nameColor = dark ? 'white' : 'var(--text, #1a1a2e)';

    this.innerHTML = `
      <div style="display:flex;align-items:center;gap:10px;">
        <div style="
          width:${size}px;
          height:${size}px;
          background:linear-gradient(135deg,#f97316,#fb923c);
          border-radius:${Math.round(size * 0.25)}px;
          display:flex;
          align-items:center;
          justify-content:center;
          box-shadow:0 4px 12px rgba(249,115,22,0.3);
          flex-shrink:0;
        ">
          <svg viewBox="0 0 32 32" fill="none" stroke="white" 
               stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"
               width="${Math.round(size * 0.6)}" height="${Math.round(size * 0.6)}">
            <line x1="4"  y1="16" x2="12" y2="16"/>
            <line x1="12" y1="9"  x2="12" y2="23"/>
            <line x1="12" y1="10.5" x2="26" y2="5"/>
            <line x1="12" y1="21.5" x2="26" y2="27"/>
            <polygon points="22,24.5 26,27 23.5,22.5" fill="white" stroke="none"/>
            <line x1="26" y1="5"  x2="26" y2="2"/>
            <line x1="26" y1="27" x2="26" y2="30"/>
          </svg>
        </div>
        ${showName ? `<span style="font-weight:700;font-size:${Math.round(size * 0.4)}px;color:${nameColor};font-family:'Plus Jakarta Sans',sans-serif;">Laboratory Reservation System</span>` : ''}
      </div>
    `;
  }
}

customElements.define('lab-logo', LabLogo);