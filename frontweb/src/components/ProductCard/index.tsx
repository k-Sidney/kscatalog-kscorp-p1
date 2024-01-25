import ProductPrice from "components/ProductPrice";
import "./styles.css";

import ProductImg from 'assets/images/product.png';

const ProductCard = () => {
  return (
    <div className="base-card product-card">
      <div className="card-top-container">
        <img src={ProductImg} alt="Nome do Produto" />
      </div>
      <div className="card-bottom-container">
        <h6>Nome do produto</h6>
        <ProductPrice/>
      </div>

    </div>
  )
};

export default ProductCard;
