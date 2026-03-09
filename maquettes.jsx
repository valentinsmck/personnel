import { useState } from "react";

const SCREENS = {
  LOGIN: "login",
  MENU_ROOT: "menu_root",
  GERER_ROOT: "gerer_root",
  LISTE_LIGUES: "liste_ligues",
  EDITER_LIGUE: "editer_ligue",
  LISTE_EMPLOYES: "liste_employes",
  GERER_EMPLOYE: "gerer_employe",
  GERER_COMPTE_EMPLOYE: "gerer_compte_employe",
};

const styles = {
  app: {
    fontFamily: "'Courier New', monospace",
    background: "#0d0d0d",
    minHeight: "100vh",
    color: "#e0e0e0",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    padding: "24px",
  },
  nav: {
    display: "flex",
    gap: "8px",
    flexWrap: "wrap",
    justifyContent: "center",
    marginBottom: "32px",
    maxWidth: "900px",
  },
  navBtn: (active) => ({
    background: active ? "#00ff88" : "#1a1a1a",
    color: active ? "#0d0d0d" : "#888",
    border: `1px solid ${active ? "#00ff88" : "#333"}`,
    padding: "6px 12px",
    fontSize: "11px",
    cursor: "pointer",
    borderRadius: "3px",
    letterSpacing: "0.05em",
    fontFamily: "'Courier New', monospace",
    transition: "all 0.15s",
  }),
  card: {
    background: "#141414",
    border: "1px solid #2a2a2a",
    borderRadius: "6px",
    padding: "32px",
    width: "100%",
    maxWidth: "520px",
  },
  title: {
    color: "#00ff88",
    fontSize: "13px",
    letterSpacing: "0.15em",
    textTransform: "uppercase",
    marginBottom: "4px",
    fontWeight: "normal",
  },
  subtitle: {
    color: "#555",
    fontSize: "11px",
    marginBottom: "28px",
    letterSpacing: "0.05em",
  },
  divider: {
    height: "1px",
    background: "#2a2a2a",
    margin: "20px 0",
  },
  label: {
    color: "#666",
    fontSize: "10px",
    letterSpacing: "0.1em",
    textTransform: "uppercase",
    marginBottom: "6px",
    display: "block",
  },
  input: {
    width: "100%",
    background: "#0d0d0d",
    border: "1px solid #2a2a2a",
    borderRadius: "3px",
    color: "#e0e0e0",
    padding: "10px 12px",
    fontSize: "13px",
    fontFamily: "'Courier New', monospace",
    marginBottom: "16px",
    boxSizing: "border-box",
    outline: "none",
  },
  btnPrimary: {
    background: "#00ff88",
    color: "#0d0d0d",
    border: "none",
    padding: "10px 24px",
    fontSize: "12px",
    fontFamily: "'Courier New', monospace",
    fontWeight: "bold",
    letterSpacing: "0.1em",
    cursor: "pointer",
    borderRadius: "3px",
    width: "100%",
  },
  btnSecondary: {
    background: "transparent",
    color: "#888",
    border: "1px solid #333",
    padding: "8px 16px",
    fontSize: "11px",
    fontFamily: "'Courier New', monospace",
    cursor: "pointer",
    borderRadius: "3px",
    letterSpacing: "0.05em",
  },
  btnDanger: {
    background: "transparent",
    color: "#ff4444",
    border: "1px solid #ff4444",
    padding: "8px 16px",
    fontSize: "11px",
    fontFamily: "'Courier New', monospace",
    cursor: "pointer",
    borderRadius: "3px",
  },
  menuItem: {
    display: "flex",
    alignItems: "center",
    padding: "14px 16px",
    border: "1px solid #222",
    borderRadius: "4px",
    marginBottom: "8px",
    cursor: "pointer",
    background: "#0d0d0d",
    transition: "border-color 0.15s",
  },
  menuIcon: {
    width: "32px",
    height: "32px",
    background: "#1a1a1a",
    borderRadius: "4px",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    marginRight: "14px",
    fontSize: "14px",
  },
  tag: (color) => ({
    display: "inline-block",
    padding: "2px 8px",
    borderRadius: "3px",
    fontSize: "10px",
    background: color === "green" ? "#00ff8820" : color === "red" ? "#ff444420" : "#ffffff10",
    color: color === "green" ? "#00ff88" : color === "red" ? "#ff4444" : "#888",
    border: `1px solid ${color === "green" ? "#00ff8840" : color === "red" ? "#ff444440" : "#333"}`,
    letterSpacing: "0.05em",
  }),
  tableRow: {
    display: "flex",
    alignItems: "center",
    padding: "10px 12px",
    borderBottom: "1px solid #1a1a1a",
    fontSize: "12px",
  },
  screenLabel: {
    position: "absolute",
    top: "-24px",
    left: "0",
    color: "#444",
    fontSize: "10px",
    letterSpacing: "0.1em",
    textTransform: "uppercase",
  },
};

// ─── SCREENS ────────────────────────────────────────────────

function LoginScreen() {
  return (
    <div style={styles.card}>
      <div style={{ textAlign: "center", marginBottom: "32px" }}>
        <div style={{ color: "#00ff88", fontSize: "24px", marginBottom: "8px" }}>⬡</div>
        <div style={styles.title}>GestionPersonnel</div>
        <div style={styles.subtitle}>Authentification requise</div>
      </div>
      <label style={styles.label}>Identifiant</label>
      <input style={styles.input} defaultValue="root" readOnly />
      <label style={styles.label}>Mot de passe</label>
      <input style={{ ...styles.input, letterSpacing: "0.2em" }} type="password" defaultValue="toor" readOnly />
      <button style={styles.btnPrimary}>SE CONNECTER</button>
      <div style={{ marginTop: "16px", textAlign: "center" }}>
        <span style={{ ...styles.tag("red"), fontSize: "10px" }}>✗ Identifiants incorrects</span>
        <span style={{ marginLeft: "8px", ...styles.tag("green"), fontSize: "10px", display: "none" }}>✓ Connexion réussie</span>
      </div>
      <div style={{ marginTop: "24px", padding: "12px", background: "#0a0a0a", borderRadius: "4px", border: "1px solid #1a1a1a" }}>
        <div style={{ color: "#444", fontSize: "10px", marginBottom: "6px" }}>RÔLES DISPONIBLES</div>
        <div style={{ color: "#555", fontSize: "11px" }}>● root — super-utilisateur</div>
        <div style={{ color: "#555", fontSize: "11px" }}>● admin — administrateur de ligue</div>
      </div>
    </div>
  );
}

function MenuRootScreen() {
  const items = [
    { icon: "👤", label: "Gérer le compte root", desc: "Modifier les informations du super-utilisateur", color: "#00ff88" },
    { icon: "🏛", label: "Gérer les ligues", desc: "Créer, modifier et supprimer des ligues", color: "#00aaff" },
    { icon: "⏻", label: "Quitter", desc: "Fermer l'application", color: "#ff4444" },
  ];
  return (
    <div style={styles.card}>
      <div style={styles.title}>Menu Principal</div>
      <div style={styles.subtitle}>Connecté en tant que root — super-utilisateur</div>
      <div style={{ display: "flex", alignItems: "center", gap: "10px", marginBottom: "24px", padding: "10px 12px", background: "#0d0d0d", border: "1px solid #1e1e1e", borderRadius: "4px" }}>
        <div style={{ width: "36px", height: "36px", background: "#00ff8820", border: "1px solid #00ff8840", borderRadius: "50%", display: "flex", alignItems: "center", justifyContent: "center", color: "#00ff88", fontSize: "16px" }}>R</div>
        <div>
          <div style={{ fontSize: "13px", color: "#e0e0e0" }}>root</div>
          <div style={{ fontSize: "10px", color: "#555" }}>root@system — super-utilisateur</div>
        </div>
        <span style={{ marginLeft: "auto", ...styles.tag("green") }}>CONNECTÉ</span>
      </div>
      {items.map((item) => (
        <div key={item.label} style={{ ...styles.menuItem }}>
          <div style={{ ...styles.menuIcon, color: item.color }}>{item.icon}</div>
          <div style={{ flex: 1 }}>
            <div style={{ fontSize: "13px", color: "#e0e0e0" }}>{item.label}</div>
            <div style={{ fontSize: "10px", color: "#555", marginTop: "2px" }}>{item.desc}</div>
          </div>
          <span style={{ color: "#444", fontSize: "12px" }}>›</span>
        </div>
      ))}
    </div>
  );
}

function GererRootScreen() {
  return (
    <div style={styles.card}>
      <div style={styles.title}>Gérer le compte root</div>
      <div style={styles.subtitle}>Modification des informations du super-utilisateur</div>
      <div style={{ padding: "14px", background: "#0d0d0d", border: "1px solid #1e1e1e", borderRadius: "4px", marginBottom: "24px" }}>
        <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "12px", fontSize: "12px" }}>
          {[["Nom", "Root"], ["Prénom", "Super"], ["Mail", "root@system.fr"], ["Password", "••••••••"]].map(([k, v]) => (
            <div key={k}>
              <div style={{ color: "#555", fontSize: "10px", marginBottom: "2px" }}>{k}</div>
              <div style={{ color: "#e0e0e0" }}>{v}</div>
            </div>
          ))}
        </div>
      </div>
      <div style={styles.divider} />
      <div style={{ color: "#555", fontSize: "10px", letterSpacing: "0.1em", textTransform: "uppercase", marginBottom: "12px" }}>Modifier un champ</div>
      {["Nom", "Prénom", "Mail", "Mot de passe"].map((field) => (
        <div key={field} style={{ display: "flex", gap: "8px", marginBottom: "8px", alignItems: "center" }}>
          <input style={{ ...styles.input, marginBottom: 0, flex: 1 }} placeholder={`Nouveau ${field.toLowerCase()}...`} />
          <button style={{ ...styles.btnSecondary, whiteSpace: "nowrap" }}>Modifier</button>
        </div>
      ))}
      <div style={styles.divider} />
      <button style={styles.btnSecondary}>← Retour</button>
    </div>
  );
}

function ListeLiguesScreen() {
  const ligues = [
    { nom: "LigueAlpha", admin: "Dupont Jean", nb: 5 },
    { nom: "LigueBeta", admin: "Martin Claire", nb: 12 },
    { nom: "LigueGamma", admin: "— aucun —", nb: 3 },
  ];
  return (
    <div style={styles.card}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start", marginBottom: "4px" }}>
        <div style={styles.title}>Ligues</div>
        <button style={{ ...styles.btnPrimary, width: "auto", padding: "6px 14px", fontSize: "11px" }}>+ Ajouter</button>
      </div>
      <div style={styles.subtitle}>{ligues.length} ligues enregistrées</div>
      <div style={{ border: "1px solid #1e1e1e", borderRadius: "4px", overflow: "hidden" }}>
        <div style={{ ...styles.tableRow, background: "#0a0a0a", color: "#555", fontSize: "10px", textTransform: "uppercase", letterSpacing: "0.1em" }}>
          <span style={{ flex: 2 }}>Nom</span>
          <span style={{ flex: 2 }}>Administrateur</span>
          <span style={{ flex: 1, textAlign: "center" }}>Employés</span>
          <span style={{ flex: 1 }}></span>
        </div>
        {ligues.map((l) => (
          <div key={l.nom} style={styles.tableRow}>
            <span style={{ flex: 2, color: "#00aaff", fontSize: "13px" }}>{l.nom}</span>
            <span style={{ flex: 2, color: l.admin.startsWith("—") ? "#444" : "#ccc" }}>{l.admin}</span>
            <span style={{ flex: 1, textAlign: "center" }}>
              <span style={styles.tag("default")}>{l.nb}</span>
            </span>
            <span style={{ flex: 1, textAlign: "right" }}>
              <button style={{ ...styles.btnSecondary, padding: "4px 10px" }}>Éditer ›</button>
            </span>
          </div>
        ))}
      </div>
      <div style={{ marginTop: "16px" }}>
        <button style={styles.btnSecondary}>← Retour</button>
      </div>
    </div>
  );
}

function EditerLigueScreen() {
  return (
    <div style={styles.card}>
      <div style={styles.title}>Éditer — LigueAlpha</div>
      <div style={styles.subtitle}>Gestion de la ligue sélectionnée</div>
      <div style={{ padding: "14px", background: "#0d0d0d", border: "1px solid #1e1e1e", borderRadius: "4px", marginBottom: "24px" }}>
        <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "12px", fontSize: "12px" }}>
          <div><div style={{ color: "#555", fontSize: "10px", marginBottom: "2px" }}>Nom</div><div style={{ color: "#00aaff" }}>LigueAlpha</div></div>
          <div><div style={{ color: "#555", fontSize: "10px", marginBottom: "2px" }}>Administrateur</div><div>Dupont Jean</div></div>
          <div><div style={{ color: "#555", fontSize: "10px", marginBottom: "2px" }}>Employés</div><div>5 membres</div></div>
        </div>
      </div>
      <div style={styles.divider} />
      <div style={{ color: "#555", fontSize: "10px", letterSpacing: "0.1em", textTransform: "uppercase", marginBottom: "12px" }}>Actions</div>
      {[
        { label: "Voir la liste des employés", icon: "👥", color: "#00aaff" },
        { label: "Renommer la ligue", icon: "✏️", color: "#e0e0e0" },
        { label: "Changer l'administrateur", icon: "🔑", color: "#ffaa00" },
      ].map((a) => (
        <div key={a.label} style={{ ...styles.menuItem, marginBottom: "8px" }}>
          <div style={{ ...styles.menuIcon }}>{a.icon}</div>
          <span style={{ color: a.color, fontSize: "13px" }}>{a.label}</span>
          <span style={{ marginLeft: "auto", color: "#444" }}>›</span>
        </div>
      ))}
      <div style={{ marginTop: "8px", display: "flex", gap: "8px" }}>
        <button style={styles.btnDanger}>🗑 Supprimer la ligue</button>
        <button style={{ ...styles.btnSecondary, marginLeft: "auto" }}>← Retour</button>
      </div>
      <div style={{ marginTop: "12px", padding: "10px 12px", background: "#0a0a0a", borderRadius: "4px", border: "1px solid #1a1a1a" }}>
        <div style={{ color: "#555", fontSize: "10px", marginBottom: "4px" }}>RENOMMER</div>
        <div style={{ display: "flex", gap: "8px" }}>
          <input style={{ ...styles.input, marginBottom: 0, flex: 1 }} placeholder="Nouveau nom de la ligue..." />
          <button style={styles.btnSecondary}>Valider</button>
        </div>
      </div>
    </div>
  );
}

function ListeEmployesScreen() {
  const employes = [
    { nom: "Dupont Jean", mail: "j.dupont@ligue.fr", arrivee: "2022-03-01", depart: "—", admin: true },
    { nom: "Martin Claire", mail: "c.martin@ligue.fr", arrivee: "2021-06-15", depart: "2024-12-31", admin: false },
    { nom: "Bernard Paul", mail: "p.bernard@ligue.fr", arrivee: "2023-01-10", depart: "—", admin: false },
  ];
  return (
    <div style={styles.card}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start", marginBottom: "4px" }}>
        <div>
          <div style={styles.title}>Employés — LigueAlpha</div>
        </div>
        <button style={{ ...styles.btnPrimary, width: "auto", padding: "6px 14px", fontSize: "11px" }}>+ Ajouter</button>
      </div>
      <div style={styles.subtitle}>{employes.length} employés</div>
      <div style={{ border: "1px solid #1e1e1e", borderRadius: "4px", overflow: "hidden" }}>
        <div style={{ ...styles.tableRow, background: "#0a0a0a", color: "#555", fontSize: "10px", textTransform: "uppercase", letterSpacing: "0.1em" }}>
          <span style={{ flex: 2 }}>Nom</span>
          <span style={{ flex: 2 }}>Mail</span>
          <span style={{ flex: 1 }}>Arrivée</span>
          <span style={{ flex: 1 }}>Départ</span>
          <span style={{ flex: 1 }}></span>
        </div>
        {employes.map((e) => (
          <div key={e.nom} style={styles.tableRow}>
            <span style={{ flex: 2 }}>
              {e.nom}
              {e.admin && <span style={{ ...styles.tag("green"), marginLeft: "6px" }}>admin</span>}
            </span>
            <span style={{ flex: 2, color: "#666", fontSize: "11px" }}>{e.mail}</span>
            <span style={{ flex: 1, color: "#888", fontSize: "11px" }}>{e.arrivee}</span>
            <span style={{ flex: 1, color: e.depart === "—" ? "#444" : "#ff8844", fontSize: "11px" }}>{e.depart}</span>
            <span style={{ flex: 1, textAlign: "right" }}>
              <button style={{ ...styles.btnSecondary, padding: "4px 10px" }}>Gérer ›</button>
            </span>
          </div>
        ))}
      </div>
      <div style={{ marginTop: "16px" }}>
        <button style={styles.btnSecondary}>← Retour</button>
      </div>
    </div>
  );
}

function GererEmployeScreen() {
  return (
    <div style={styles.card}>
      <div style={styles.title}>Gérer — Dupont Jean</div>
      <div style={styles.subtitle}>LigueAlpha · Administrateur</div>
      <div style={{ padding: "14px", background: "#0d0d0d", border: "1px solid #1e1e1e", borderRadius: "4px", marginBottom: "24px" }}>
        <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "10px", fontSize: "12px" }}>
          {[["Nom", "Dupont"], ["Prénom", "Jean"], ["Mail", "j.dupont@ligue.fr"], ["Arrivée", "2022-03-01"], ["Départ", "—"], ["Rôle", "Admin LigueAlpha"]].map(([k, v]) => (
            <div key={k}>
              <div style={{ color: "#555", fontSize: "10px", marginBottom: "2px" }}>{k}</div>
              <div style={{ color: v === "—" ? "#444" : "#e0e0e0" }}>{v}</div>
            </div>
          ))}
        </div>
      </div>
      <div style={styles.divider} />
      <div style={{ ...styles.menuItem }}>
        <div style={styles.menuIcon}>⚙️</div>
        <div>
          <div style={{ fontSize: "13px" }}>Modifier le compte</div>
          <div style={{ fontSize: "10px", color: "#555" }}>Nom, prénom, mail, password, dates</div>
        </div>
        <span style={{ marginLeft: "auto", color: "#444" }}>›</span>
      </div>
      <div style={{ marginTop: "8px", display: "flex", gap: "8px" }}>
        <button style={styles.btnDanger}>🗑 Supprimer l'employé</button>
        <button style={{ ...styles.btnSecondary, marginLeft: "auto" }}>← Retour</button>
      </div>
    </div>
  );
}

function GererCompteEmployeScreen() {
  const fields = [
    { label: "Nom", value: "Dupont", type: "text" },
    { label: "Prénom", value: "Jean", type: "text" },
    { label: "Mail", value: "j.dupont@ligue.fr", type: "email" },
    { label: "Mot de passe", value: "••••••••", type: "password" },
    { label: "Date d'arrivée", value: "2022-03-01", type: "date" },
    { label: "Date de départ", value: "", type: "date", nullable: true },
  ];
  return (
    <div style={styles.card}>
      <div style={styles.title}>Modifier le compte — Dupont Jean</div>
      <div style={styles.subtitle}>Modification des informations de l'employé</div>
      {fields.map((f) => (
        <div key={f.label} style={{ marginBottom: "14px" }}>
          <label style={styles.label}>{f.label}{f.nullable && <span style={{ color: "#444", marginLeft: "6px" }}>(optionnel)</span>}</label>
          <div style={{ display: "flex", gap: "8px" }}>
            <input style={{ ...styles.input, marginBottom: 0, flex: 1 }} defaultValue={f.value} placeholder={f.nullable ? "Laisser vide = en poste" : ""} />
            <button style={{ ...styles.btnSecondary, whiteSpace: "nowrap" }}>✓ Valider</button>
          </div>
        </div>
      ))}
      <div style={styles.divider} />
      <button style={styles.btnSecondary}>← Retour</button>
    </div>
  );
}

const SCREEN_MAP = {
  [SCREENS.LOGIN]: LoginScreen,
  [SCREENS.MENU_ROOT]: MenuRootScreen,
  [SCREENS.GERER_ROOT]: GererRootScreen,
  [SCREENS.LISTE_LIGUES]: ListeLiguesScreen,
  [SCREENS.EDITER_LIGUE]: EditerLigueScreen,
  [SCREENS.LISTE_EMPLOYES]: ListeEmployesScreen,
  [SCREENS.GERER_EMPLOYE]: GererEmployeScreen,
  [SCREENS.GERER_COMPTE_EMPLOYE]: GererCompteEmployeScreen,
};

const SCREEN_LABELS = {
  [SCREENS.LOGIN]: "1. Connexion",
  [SCREENS.MENU_ROOT]: "2. Menu Root",
  [SCREENS.GERER_ROOT]: "3. Gestion Root",
  [SCREENS.LISTE_LIGUES]: "4. Liste Ligues",
  [SCREENS.EDITER_LIGUE]: "5. Éditer Ligue",
  [SCREENS.LISTE_EMPLOYES]: "6. Liste Employés",
  [SCREENS.GERER_EMPLOYE]: "7. Gérer Employé",
  [SCREENS.GERER_COMPTE_EMPLOYE]: "8. Modifier Compte",
};

export default function App() {
  const [current, setCurrent] = useState(SCREENS.LOGIN);
  const Screen = SCREEN_MAP[current];

  return (
    <div style={styles.app}>
      <div style={{ textAlign: "center", marginBottom: "24px" }}>
        <div style={{ color: "#00ff88", fontSize: "11px", letterSpacing: "0.2em", textTransform: "uppercase" }}>
          Maquettes Interface — GestionPersonnel
        </div>
        <div style={{ color: "#333", fontSize: "10px", marginTop: "4px" }}>
          Cliquez sur une fenêtre pour la visualiser
        </div>
      </div>
      <nav style={styles.nav}>
        {Object.entries(SCREEN_LABELS).map(([key, label]) => (
          <button
            key={key}
            style={styles.navBtn(current === key)}
            onClick={() => setCurrent(key)}
          >
            {label}
          </button>
        ))}
      </nav>
      <div style={{ position: "relative", width: "100%", maxWidth: "520px" }}>
        <Screen />
      </div>
    </div>
  );
}
