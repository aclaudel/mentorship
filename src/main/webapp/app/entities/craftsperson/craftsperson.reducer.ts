import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICraftsperson, defaultValue } from 'app/shared/model/craftsperson.model';

export const ACTION_TYPES = {
  FETCH_CRAFTSPERSON_LIST: 'craftsperson/FETCH_CRAFTSPERSON_LIST',
  FETCH_CRAFTSPERSON: 'craftsperson/FETCH_CRAFTSPERSON',
  CREATE_CRAFTSPERSON: 'craftsperson/CREATE_CRAFTSPERSON',
  UPDATE_CRAFTSPERSON: 'craftsperson/UPDATE_CRAFTSPERSON',
  DELETE_CRAFTSPERSON: 'craftsperson/DELETE_CRAFTSPERSON',
  RESET: 'craftsperson/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICraftsperson>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type CraftspersonState = Readonly<typeof initialState>;

// Reducer

export default (state: CraftspersonState = initialState, action): CraftspersonState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CRAFTSPERSON_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CRAFTSPERSON):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CRAFTSPERSON):
    case REQUEST(ACTION_TYPES.UPDATE_CRAFTSPERSON):
    case REQUEST(ACTION_TYPES.DELETE_CRAFTSPERSON):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CRAFTSPERSON_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CRAFTSPERSON):
    case FAILURE(ACTION_TYPES.CREATE_CRAFTSPERSON):
    case FAILURE(ACTION_TYPES.UPDATE_CRAFTSPERSON):
    case FAILURE(ACTION_TYPES.DELETE_CRAFTSPERSON):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CRAFTSPERSON_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_CRAFTSPERSON):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CRAFTSPERSON):
    case SUCCESS(ACTION_TYPES.UPDATE_CRAFTSPERSON):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CRAFTSPERSON):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/craftspeople';

// Actions

export const getEntities: ICrudGetAllAction<ICraftsperson> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CRAFTSPERSON_LIST,
    payload: axios.get<ICraftsperson>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ICraftsperson> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CRAFTSPERSON,
    payload: axios.get<ICraftsperson>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICraftsperson> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CRAFTSPERSON,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<ICraftsperson> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CRAFTSPERSON,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICraftsperson> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CRAFTSPERSON,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
