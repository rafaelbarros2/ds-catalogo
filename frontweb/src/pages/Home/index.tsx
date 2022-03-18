import{ReactComponent as MainImage } from  'assets/images/main-image.svg'
import ButtonIcon from 'components/ButtonIcon';
import NavBar from 'components/NavBar';
import './styles.css'
const Home = () => {
  return(
    <>
    <NavBar/>
    <div className="home-container"> 
        <div className="home-card">
            <div className="home-content-container">
              <div>
                <h1>Conheça o melhor catálago de produtos</h1>
                <p>Ajudaremos você a encontrar os melhores produtos disponiveis do mercado</p>
                </div>
                <ButtonIcon/>
            </div>
            <div className="home-image-container">
                <MainImage/>
            </div>
        </div>
    </div>
  </>
  );
}

export default Home;
