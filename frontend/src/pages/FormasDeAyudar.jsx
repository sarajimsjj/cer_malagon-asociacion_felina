import './FormasDeAyudar.css'

// TODO: sustituir por las URLs reales antes de publicar la página.
const ENLACES = {
  contacto: '#',
  teaming: 'https://www.teaming.net/metodocer-grupo',
  gofundme: 'https://gofund.me/ad9781e72',
  amazon: '#',
  kiwoko: '#',
  guau: '#',
}

export default function FormasDeAyudar() {
  return (
    <div className="pagina">
      <header className="cabecera">
        <h1 className="cabecera__titulo">Formas de ayudar</h1>
        <p className="cabecera__descripcion">
          Hay muchas maneras de echarnos una mano, dentro y fuera del refugio. Elige la que mejor encaje contigo.
        </p>
      </header>

      <div className="formas-ayudar__grid">
        <section className="formas-ayudar__tarjeta">
          <h2 className="formas-ayudar__titulo">Dona material</h2>
          <p className="formas-ayudar__texto">
            ¿Tienes mantas, toallas, transportines, rascadores, muebles para gatos o incluso manualidades hechas por
            ti? También aceptamos donaciones de material para el refugio. Contacta con nosotras y te contamos cómo
            hacérnoslo llegar.
          </p>
          <a href={ENLACES.contacto} className="boton boton--secundario">
            Contactar
          </a>
        </section>

        <section className="formas-ayudar__tarjeta">
          <h2 className="formas-ayudar__titulo">Sé casa de acogida</h2>
          <p className="formas-ayudar__texto">
            Acoger temporalmente a un gato en tu casa mientras encuentra una familia definitiva es una de las ayudas
            más valiosas que puedes ofrecernos.
          </p>
          <a href={ENLACES.contacto} className="boton boton--secundario">
            Contactar
          </a>
        </section>

        <section className="formas-ayudar__tarjeta">
          <h2 className="formas-ayudar__titulo">Colabora económicamente</h2>
          <p className="formas-ayudar__texto">
            Cada donación, puntual o mensual, nos ayuda a cubrir veterinario, alimentación y cuidados de los gatos
            del refugio.
          </p>
          <div className="formas-ayudar__acciones">
            <a href={ENLACES.teaming} className="boton boton--secundario">
              Teaming
            </a>
            <a href={ENLACES.gofundme} className="boton boton--secundario">
              GoFundMe
            </a>
          </div>
        </section>

        <section className="formas-ayudar__tarjeta">
          <h2 className="formas-ayudar__titulo">Compra lo que necesitamos</h2>
          <p className="formas-ayudar__texto">
            También puedes ayudarnos comprando directamente material para el refugio desde nuestras listas de
            deseos.
          </p>
          <div className="formas-ayudar__acciones">
            <a href={ENLACES.amazon} className="boton boton--secundario">
              Amazon
            </a>
            <a href={ENLACES.kiwoko} className="boton boton--secundario">
              Kiwoko
            </a>
            <a href={ENLACES.guau} className="boton boton--secundario">
              Guau
            </a>
          </div>
        </section>
      </div>
    </div>
  )
}
